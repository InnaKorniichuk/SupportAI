package com.supportai.app.service;

import com.supportai.app.dto.ticket.*;
import com.supportai.app.model.Message;
import com.supportai.app.model.Ticket;
import com.supportai.app.model.TicketStatus;
import com.supportai.app.model.User;
import com.supportai.app.repository.MessageRepository;
import com.supportai.app.repository.TicketRepository;
import com.supportai.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;
    private final MessageRepository messageRepository;

    public TicketService(TicketRepository ticketRepository,
                         UserRepository userRepository,
                         TicketMapper ticketMapper,
                         MessageRepository messageRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.ticketMapper = ticketMapper;
        this.messageRepository = messageRepository;
    }

    public Ticket create(TicketCreateDto dto, Long customerId, Long agentId){
        User customer = userRepository.findById(customerId).orElseThrow(() -> new NoSuchElementException("Ticket not found"));

        Ticket ticket = ticketMapper.toEntity(dto);
        ticket.setCreatedAt(LocalDateTime.now());
        if (agentId != null) {
            ticket.setAssignedAgent(
                    userRepository.findById(agentId).orElseThrow(() -> new NoSuchElementException("Agent not found"))
            );
        }
        ticket.setCustomer(customer);
        ticket.setTicketStatus(TicketStatus.OPEN);

        return ticketRepository.save(ticket);
    }

    public TicketResponseDto readById(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new NoSuchElementException("Ticket not found"));

        return ticketMapper.toDto(ticket);
    }

    public TicketResponseDto changeStatus(Long ticketId, TicketStatus status){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new NoSuchElementException("Ticket not found"));
        ticket.setTicketStatus(status);

        ticketRepository.save(ticket);

        return ticketMapper.toDto(ticket);
    }

    public TicketDetailsDto getDetails(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        List<Message> messages =
                messageRepository.findAllByTicketOrderBySentAtAsc(ticket);

        return ticketMapper.toDetailsDto(ticket, messages);
    }

    public void delete(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new NoSuchElementException("Ticket not found"));
        ticketRepository.delete(ticket);
    }

    public TicketResponseDto update(Long ticketId, TicketUpdateDto dto){
        Ticket existing = ticketRepository.findById(ticketId).orElseThrow(() -> new NoSuchElementException("Ticket not found"));

        existing.setTicketStatus(dto.getTicketStatus());

        if (dto.getAssignedAgentId() != null) {
            User agent = userRepository.findById(dto.getAssignedAgentId())
                    .orElseThrow(() -> new NoSuchElementException("Agent not found"));
            existing.setAssignedAgent(agent);
        }

        Ticket updated = ticketRepository.save(existing);
        return ticketMapper.toDto(updated);
    }

    public List<TicketResponseDto> findByCustomerId(Long customerId) {
        return ticketRepository.findByCustomerId(customerId)
                .stream()
                .map(ticketMapper::toDto)
                .toList();
    }
}
