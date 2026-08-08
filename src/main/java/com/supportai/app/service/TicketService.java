package com.supportai.app.service;

import com.supportai.app.dto.ticket.TicketCreateDto;
import com.supportai.app.dto.ticket.TicketMapper;
import com.supportai.app.dto.ticket.TicketResponseDto;
import com.supportai.app.dto.ticket.TicketUpdateDto;
import com.supportai.app.model.Ticket;
import com.supportai.app.model.TicketStatus;
import com.supportai.app.model.User;
import com.supportai.app.repository.TicketRepository;
import com.supportai.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, TicketMapper ticketMapper){
        this.ticketRepository=ticketRepository;
        this.userRepository=userRepository;
        this.ticketMapper=ticketMapper;
    }

    public TicketResponseDto  create(TicketCreateDto dto, Long customerId, Long agentId){
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

        Ticket saved = ticketRepository.save(ticket);
        return ticketMapper.toDto(saved);
    }

    public TicketResponseDto readById(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new NoSuchElementException("Ticket not found"));

        return ticketMapper.toDto(ticket);
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
}
