package com.supportai.app.service;

import com.supportai.app.dto.message.MessageCreateDto;
import com.supportai.app.dto.message.MessageMapper;
import com.supportai.app.dto.message.MessageResponseDto;
import com.supportai.app.model.Message;
import com.supportai.app.model.Ticket;
import com.supportai.app.model.User;
import com.supportai.app.repository.MessageRepository;
import com.supportai.app.repository.TicketRepository;
import com.supportai.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public MessageService(MessageRepository messageRepository,
                          MessageMapper messageMapper,
                          UserRepository userRepository,
                          TicketRepository ticketRepository){
        this.messageRepository=messageRepository;
        this.messageMapper=messageMapper;
        this.ticketRepository=ticketRepository;
        this.userRepository=userRepository;
    }

    public Message create(MessageCreateDto dto, Long userId, Long ticketId){
        Message message = messageMapper.toEntity(dto);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (!ticket.getCustomer().getId().equals(userId)
                && !ticket.getAssignedAgent().getId().equals(userId))
            throw new IllegalStateException("User cannot access this ticket");

        message.setTicket(ticket);
        message.setSender(user);
        message.setSentAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public MessageResponseDto read(Long messageId){
        Message message = messageRepository.findById(messageId).orElseThrow();
        return  messageMapper.toDto(message);
    }

    public void delete(Long messageId){
        Message message = messageRepository.findById(messageId).orElseThrow();
       messageRepository.delete(message);
    }
}
