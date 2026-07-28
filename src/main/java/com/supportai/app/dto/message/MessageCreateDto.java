package com.supportai.app.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageCreateDto {
    @NotNull
    private Long ticketId;

    @NotBlank
    @Size(max = 2000)
    private String content;
}
