package com.supportai.app.service;

import com.supportai.app.dto.user.UserMapper;
import com.supportai.app.dto.user.UserRegistrationDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.Role;
import com.supportai.app.model.User;
import com.supportai.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.userMapper=userMapper;
        this.passwordEncoder=passwordEncoder;
    }

    public User create(UserRegistrationDto dto){
        User user = userMapper.toEntity(dto);

        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public UserResponseDto readByEmail(String email){
        return userMapper.toDto(userRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    public UserResponseDto readById(Long id){
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found")));
    }

    public void delete(Long id, String currentUserEmail) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found"));

        if (user.getEmail().equals(currentUserEmail)) {
            throw new IllegalArgumentException(
                    "You cannot delete your own account"
            );
        }

        userRepository.delete(user);
    }

    public UserResponseDto update(Long userId, UserRegistrationDto dto){
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        existing.setNickname(dto.getNickname());
        existing.setEmail(dto.getEmail());
        existing.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userMapper.toDto(userRepository.save(existing));
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserResponseDto> findAgents() {
        return userRepository.findByRole(Role.AGENT)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}
