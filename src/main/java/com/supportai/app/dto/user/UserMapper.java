package com.supportai.app.dto.user;

import com.supportai.app.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDto toDto(User user){
        UserResponseDto dto = new UserResponseDto();

        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        dto.setRole(user.getRole().name());

        return dto;
    }

    public User toEntity(UserRegistrationDto dto) {
        User user = new User();

        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setPassword(dto.getPassword());

        return user;
    }
}
