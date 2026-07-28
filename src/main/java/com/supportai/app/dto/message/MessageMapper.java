package com.supportai.app.dto.message;

import com.supportai.app.dto.user.UserMapper;
import com.supportai.app.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageMapper {
    private final UserMapper userMapper;

    public MessageResponseDto toDto(Message message){
        MessageResponseDto dto = new MessageResponseDto();

        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        dto.setSender(userMapper.toDto(message.getSender()));

        return dto;
    }

    public Message toEntity(MessageCreateDto dto){
        Message message = new Message();

        message.setContent(dto.getContent());

        return message;
    }
}
