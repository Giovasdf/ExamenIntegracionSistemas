package com.example.REDAFI.controller;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Controller
public class MessageWebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageWebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @JmsListener(destination = "data-controller-queue")
    public void updateMessages(TextMessage message) {
        try {
            String text = message.getText();

            // Crear un objeto JSON con el mensaje recibido utilizando Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(Map.of("body", text));

            simpMessagingTemplate.convertAndSend("/queue/messages", jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

