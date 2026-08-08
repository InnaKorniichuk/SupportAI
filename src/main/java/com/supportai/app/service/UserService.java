package com.supportai.app.service;

import com.supportai.app.dto.user.UserMapper;
import com.supportai.app.dto.user.UserRegistrationDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.Role;
import com.supportai.app.model.User;
import com.supportai.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.userMapper=userMapper;
        this.passwordEncoder=passwordEncoder;
    }

    public UserResponseDto create(UserRegistrationDto dto){
        User user = userMapper.toEntity(dto);

        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }

    public UserResponseDto read(Long id){
        return userMapper.toDto(userRepository.findById(id).orElseThrow());
    }

    public void delete(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepository.delete(user);
    }

    public UserResponseDto update(Long userId, UserResponseDto dto){
        User existing = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));

        existing.setNickname(dto.getNickname());
        existing.setEmail(dto.getEmail());

        return userMapper.toDto(userRepository.save(existing));
    }
}
