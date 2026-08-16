package com.supportai.app.service;

import com.supportai.app.dto.ticket.TicketDetailsDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final TicketService ticketService;

    public AiService(ChatClient.Builder chatClientBuilder,
                     TicketService ticketService) {
        this.chatClient = chatClientBuilder.build();
        this.ticketService = ticketService;
    }

    public String generateResponse(Long ticketId) {

        TicketDetailsDto ticket =
                ticketService.getDetails(ticketId);

        String conversation = ticket.getMessages()
                .stream()
                .map(message ->
                        message.getSender().getNickname()
                                + ": "
                                + message.getContent())
                .collect(Collectors.joining("\n"));

        return chatClient
                .prompt()
                .system("""
                        You are an AI assistant helping a customer
                        support agent.

                        Read the customer conversation and suggest
                        a professional response for the support agent.

                        Do not invent information.
                        Keep the response helpful and concise.
                        """)
                .user(conversation)
                .call()
                .content();
    }
}