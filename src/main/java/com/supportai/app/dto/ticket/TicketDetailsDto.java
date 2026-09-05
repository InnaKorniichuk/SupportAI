package com.supportai.app.dto.ticket;

import com.supportai.app.dto.message.MessageResponseDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketDetailsDto {
    private Long id;
    private String title;
    private String description;
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
    private UserResponseDto customer;
    private UserResponseDto assignedAgent;
    private List<MessageResponseDto> messages;
}
