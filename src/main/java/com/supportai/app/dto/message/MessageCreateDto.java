package com.supportai.app.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageCreateDto {
    @NotBlank
    @Size(max = 2000)
    private String content;
}
