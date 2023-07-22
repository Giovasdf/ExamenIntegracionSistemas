package com.example.REDAFI;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.jms.annotation.JmsListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DataControllerRoute extends RouteBuilder {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public DataControllerRoute(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void configure() throws Exception {
        from("activemq:queue:data-controller-queue") // Ruta para recibir mensajes desde la cola "data-controller-queue" de ActiveMQ
                .log("Mensaje recibido desde la cola: ${body}") // Simplemente imprimirá el mensaje recibido
                .process(exchange -> {
                    String messageContent = exchange.getIn().getBody(String.class);
                    // Eliminar caracteres nulos del mensaje
                    messageContent = messageContent.replaceAll("\\x00", "");
                    // Enviar el mensaje al controlador WebSocket
                    simpMessagingTemplate.convertAndSend("/queue/messages", "{\"body\":\"" + messageContent + "\"}");

                    // Transformar el mensaje a XML, JSON y TXT
                    String xmlMessage = convertToXML(messageContent);
                    String jsonMessage = convertToJSON(messageContent);
                    String txtMessage = convertToTXT(messageContent);

                    // Enviar los mensajes transformados a través del controlador WebSocket
                    simpMessagingTemplate.convertAndSend("/queue/messages", "{\"format\":\"xml\",\"content\":\"" + xmlMessage + "\"}");
                    simpMessagingTemplate.convertAndSend("/queue/messages", "{\"format\":\"json\",\"content\":\"" + jsonMessage + "\"}");
                    simpMessagingTemplate.convertAndSend("/queue/messages", "{\"format\":\"txt\",\"content\":\"" + txtMessage + "\"}");

                    // Enviar los datos a la API
                    sendDataToAPI(messageContent);
                });
    }


    private String convertToXML(String messageContent) {
        // Agregar la cabecera XML al mensaje
        StringBuilder xmlMessage = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>\n");

        // Dividir el mensaje en líneas
        String[] lines = messageContent.split("\\r?\\n");

        // Abrir el elemento raíz del XML
        xmlMessage.append("<data>\n");

        // Recorrer las líneas del mensaje y agregarlas al mensaje en formato XML
        for (String line : lines) {
            xmlMessage.append("  ").append(line).append("\n");
        }

        // Cerrar el elemento raíz del XML
        xmlMessage.append("</data>");

        // Retornar el mensaje completo en formato XML
        return xmlMessage.toString();
    }

    private String convertToJSON(String messageContent) {
        // En este caso, suponemos que el mensaje ya es una cadena JSON válida, por lo que no es necesario realizar cambios.
        // Si el mensaje no es un JSON válido, se devolverá el mensaje original como un objeto JSON con una propiedad "message".
        return messageContent;
    }

    private String convertToTXT(String messageContent) {
        // Dividir el mensaje en líneas
        String[] lines = messageContent.split("\\r?\\n");

        // Inicializar una variable para el mensaje en formato TXT
        StringBuilder txtMessage = new StringBuilder();

        // Recorrer las líneas del mensaje y agregarlas al mensaje en formato TXT con el prefijo "TXT: "
        for (String line : lines) {
            txtMessage.append("TXT: ").append(line).append("\n");
        }

        // Retornar el mensaje completo en formato TXT
        return txtMessage.toString();
    }

    private void sendDataToAPI(String messageContent) {
        try {
            // Obtener los valores de los campos desde el mensaje recibido
            String tipoCuenta = getValueFromMessage(messageContent, "Tipo de Cuenta:");
            String usuario = getValueFromMessage(messageContent, "Usuario:");
            String numeroCta = getValueFromMessage(messageContent, "NumeroCuenta:");

            // Construir los parámetros del cuerpo en formato "multipart/form-data"
            MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
            bodyParams.add("tipoCuenta", tipoCuenta);
            bodyParams.add("usuario", usuario);
            bodyParams.add("numeroCta", numeroCta);

            // Construir los encabezados de la solicitud
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Crear la entidad de la solicitud con los parámetros del cuerpo y los encabezados
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

            // Construir la URL de la API
            String apiUrl = "http://127.0.0.1:8090/api/collections/cuenta/records";

            // Enviar la solicitud POST a la API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Obtener la respuesta de la API
            String apiResponse = responseEntity.getBody();
            // Puedes hacer algo con la respuesta si es necesario

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Función para obtener el valor de un campo específico del mensaje
    private String getValueFromMessage(String messageContent, String fieldName) {
        int startIndex = messageContent.indexOf(fieldName);
        if (startIndex != -1) {
            startIndex += fieldName.length();
            int endIndex = messageContent.indexOf("\n", startIndex);
            if (endIndex != -1) {
                return messageContent.substring(startIndex, endIndex).trim();
            }
        }
        return "";
    }


}
