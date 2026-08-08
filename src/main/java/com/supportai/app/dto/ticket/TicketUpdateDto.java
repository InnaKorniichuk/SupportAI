package com.supportai.app.dto.ticket;

import com.supportai.app.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketUpdateDto {
    @NotNull
    private TicketStatus ticketStatus;
    private Long assignedAgentId;
}
