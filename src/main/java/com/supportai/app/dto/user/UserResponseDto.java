package com.supportai.app.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String email;
    private String role;
}
