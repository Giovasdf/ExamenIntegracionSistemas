package com.example.REDAFI.controller;

import com.example.REDAFI.models.User;
import com.example.REDAFI.models.UserResponse;
import jakarta.jms.*;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClientException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.StreamSupport;

@Controller // Cambiar de @RestController a @Controller
public class DataController {
    private final RestTemplate restTemplate;
    private final ProducerTemplate producerTemplate;
    private final JmsTemplate jmsTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public DataController(RestTemplate restTemplate, ProducerTemplate producerTemplate, JmsTemplate jmsTemplate, SimpMessagingTemplate simpMessagingTemplate) {
        this.restTemplate = restTemplate;
        this.producerTemplate = producerTemplate;
        this.jmsTemplate = jmsTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    @GetMapping("/")
    public String showForm(Model model) {

        try {
            // Llamar a la API y obtener la lista de usuarios
            String apiUrl = "http://127.0.0.1:8090/api/collections/users/records";
            ResponseEntity<UserResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<UserResponse>() {}
            );
            List<User> users = response.getBody().getItems();

            // Agregar la lista de usuarios al modelo para mostrarlos en el select
            model.addAttribute("users", users);

            return "data-form";
        } catch (RestClientException e) {
            // Mostrar detalles del error en la consola
            e.printStackTrace();
            // Agregar un mensaje de error al modelo para mostrar en la vista
            model.addAttribute("errorMessage", "Error al llamar a la API: " + e.getMessage());
        }

        // Agregar una declaración de retorno para el caso en que se capture una excepción
        return "data-form";
    }

    @PostMapping("/processData")
    public String processData(@RequestParam(name = "tipoCuenta") String tipoCuenta,
                              @RequestParam(name = "usuario") String usuario,
                              @RequestParam(name = "data") String data,
                              Model model) {
        try {
            // Realizar alguna operación con los datos ingresados
            String messageContent = "Tipo de Cuenta: " + tipoCuenta + "\nUsuario: " + usuario + "\nNumeroCuenta: " + data;

            // Obtener los datos faltantes de la API usando el ID del usuario
            String apiUrl = "http://127.0.0.1:8090/api/collections/users/records/" + usuario;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<User> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<User>() {}
            );
            User user = response.getBody();

            // Agregar los datos faltantes al mensaje
            messageContent += "\nID: " + user.getId() +
                    "\nNombre: " + user.getNombre() +
                    "\nApellido: " + user.getApellido() +
                    "\nRUT: " + user.getRut();

            // Enviar el mensaje a la cola "data-controller-queue" de ActiveMQ para que sea procesado por la ruta
            producerTemplate.sendBody("activemq:queue:data-controller-queue", messageContent);

            // Luego, redirigir a la página de resultado
            return "result";
        } catch (RestClientException e) {
            // Mostrar detalles del error en la consola
            e.printStackTrace();
            // Agregar un mensaje de error al modelo para mostrar en la vista
            model.addAttribute("errorMessage", "Error al llamar a la API: " + e.getMessage());
        } catch (Exception e) {
            // Mostrar detalles del error en la consola
            e.printStackTrace();
            // Agregar un mensaje de error al modelo para mostrar en la vista
            model.addAttribute("errorMessage", "Error al procesar los datos: " + e.getMessage());
        }

        // Agregar una declaración de retorno para el caso en que se capture una excepción
        return "data-form";
    }

    @GetMapping("/messages")
    public String listMessages(Model model) {
        List<String> messages = new ArrayList<>();

        try {
            Enumeration<?> enumeration = jmsTemplate.browseSelected("data-controller-queue", "JMSType IS NOT NULL",
                    (session, browser) -> browser.getEnumeration());

            while (enumeration.hasMoreElements()) {
                Message message = (Message) enumeration.nextElement();
                if (message instanceof TextMessage) {
                    String text = ((TextMessage) message).getText();
                    messages.add(text);

                    // Enviar el mensaje como objeto JSON a través de WebSocket
                    simpMessagingTemplate.convertAndSend("/queue/messages", "{\"body\":\"" + text + "\"}");
                }
            }
        } catch (JMSException e) {
            // Manejar la excepción aquí (puedes imprimir el mensaje de error o realizar alguna acción)
            e.printStackTrace();
        }

        model.addAttribute("messages", messages);
        return "message-list";
    }


}
