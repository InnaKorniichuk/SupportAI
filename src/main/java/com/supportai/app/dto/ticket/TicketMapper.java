package com.supportai.app.dto.ticket;

import com.supportai.app.dto.message.MessageMapper;
import com.supportai.app.dto.user.UserMapper;
import com.supportai.app.model.Message;
import com.supportai.app.model.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TicketMapper {
    private final UserMapper userMapper;
    private final MessageMapper messageMapper;

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

    public TicketDetailsDto toDetailsDto(
            Ticket ticket,
            List<Message> messages) {

        TicketDetailsDto dto = new TicketDetailsDto();

        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setTicketStatus(ticket.getTicketStatus());
        dto.setCreatedAt(ticket.getCreatedAt());

        dto.setCustomer(
                userMapper.toDto(ticket.getCustomer())
        );

        if (ticket.getAssignedAgent() != null) {
            dto.setAssignedAgent(
                    userMapper.toDto(ticket.getAssignedAgent())
            );
        }

        dto.setMessages(
                messages.stream()
                        .map(messageMapper::toDto)
                        .toList()
        );

        return dto;
    }
}
