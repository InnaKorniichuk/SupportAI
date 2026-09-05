package com.supportai.app.configuration;

import com.supportai.app.model.Role;
import com.supportai.app.model.User;
import com.supportai.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner createUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                User admin = new User();

                admin.setNickname("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }

            if (userRepository.findByEmail("agent@gmail.com").isEmpty()) {

                User agent = new User();

                agent.setNickname("agent");
                agent.setEmail("agent@gmail.com");
                agent.setPassword(
                        passwordEncoder.encode("agent123")
                );
                agent.setRole(Role.AGENT);

                userRepository.save(agent);
            }
        };
    }
}