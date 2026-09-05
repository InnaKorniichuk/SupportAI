package com.supportai.app.service;

import com.supportai.app.dto.ticket.TicketDetailsDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final TicketService ticketService;

    public AiService(
            ChatClient.Builder chatClientBuilder,
            TicketService ticketService,
            VectorStore vectorStore) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore).build()
                )
                .build();

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

                        Use the provided knowledge base when it is relevant.

                        Read the customer conversation and suggest
                        a professional response for the support agent.

                        Do not invent information.
                        If the knowledge base does not contain enough
                        information to answer, say that the information
                        is unavailable.

                        Keep the response helpful and concise.
                        """)
                .user("""
                        Here is the current customer conversation:

                        %s
                        """.formatted(conversation))
                .call()
                .content();
    }
}