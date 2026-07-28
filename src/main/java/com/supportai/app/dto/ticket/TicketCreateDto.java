package com.supportai.app.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketCreateDto {
    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 350)
    private String description;
}
