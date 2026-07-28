package com.supportai.app.dto.ticket;

import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponseDto {
    private Long id;
    private String title;
    private String description;
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
    private UserResponseDto customer;
    private UserResponseDto assignedAgent;
}
