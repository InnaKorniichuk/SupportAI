package com.supportai.app.dto.message;

import com.supportai.app.dto.user.UserResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private Long id;
    private UserResponseDto sender;
    private String content;
    private LocalDateTime sentAt;
}
