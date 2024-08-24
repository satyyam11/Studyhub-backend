package com.studyhub.controller;

import com.studyhub.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat/{hutId}")
    @SendTo("/topic/{hutId}")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
