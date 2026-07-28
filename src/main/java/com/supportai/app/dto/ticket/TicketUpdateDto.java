package com.supportai.app.dto.ticket;

import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.TicketStatus;
import lombok.Data;

@Data
public class TicketUpdateDto {
    private TicketStatus ticketStatus;
    private Long assignedAgentId;
}
