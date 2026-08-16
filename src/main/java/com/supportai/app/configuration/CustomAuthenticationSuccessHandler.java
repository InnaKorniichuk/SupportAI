package com.supportai.app.configuration;

import com.supportai.app.model.Role;
import com.supportai.app.model.User;
import com.supportai.app.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (user.getRole().equals(Role.ADMIN)) {
            response.sendRedirect("/users/all");
            return;
        }

        if (user.getRole().equals(Role.AGENT)) {
            response.sendRedirect("/agent");
            return;
        }

        response.sendRedirect("/users/" + user.getId() + "/read");
    }
}