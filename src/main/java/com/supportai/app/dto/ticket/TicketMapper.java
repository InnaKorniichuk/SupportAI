package com.supportai.app.dto.ticket;

import com.supportai.app.dto.user.UserMapper;
import com.supportai.app.model.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TicketMapper {
    private final UserMapper userMapper;

    public TicketResponseDto toDto(Ticket ticket){
        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(ticket.getId());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setDescription(ticket.getDescription());
        dto.setTitle(ticket.getTitle());
        dto.setTicketStatus(ticket.getTicketStatus());
        dto.setCustomer(userMapper.toDto(ticket.getCustomer()));

        if (ticket.getAssignedAgent() != null)
         dto.setAssignedAgent(userMapper.toDto(ticket.getAssignedAgent()));

        return dto;
    }

    public Ticket toEntity(TicketCreateDto dto) {
        Ticket ticket = new Ticket();

        ticket.setDescription(dto.getDescription());
        ticket.setTitle(dto.getTitle());

        return ticket;
    }
}
