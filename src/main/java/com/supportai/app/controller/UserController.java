package com.supportai.app.controller;

import com.supportai.app.dto.ticket.TicketResponseDto;
import com.supportai.app.dto.user.UserRegistrationDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.User;
import com.supportai.app.service.TicketService;
import com.supportai.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TicketService ticketService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UserController(UserService userService, TicketService ticketService, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository){
        this.userService = userService;
        this.ticketService = ticketService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/create")
    public String crate(Model model){
        model.addAttribute("user", new UserRegistrationDto());

        return "user/create";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute("user") UserRegistrationDto userDto,
            HttpServletRequest request,
            HttpServletResponse response) {

        User user = userService.create(userDto);

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDto.getEmail(),
                                userDto.getPassword()
                        )
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                request,
                response
        );

        return "redirect:/users/" + user.getId() + "/read";
    }

    @GetMapping("/{id}/read")
    public String read(@PathVariable Long id,
                       Model model,
                       Principal principal){
        String email = principal.getName();

        UserResponseDto currentUser = userService.readByEmail(email);
        boolean isOwner = currentUser.getId().equals(id);
        boolean isAgent = currentUser.getRole().equals("AGENT");
        boolean isAdmin = currentUser.getRole().equals("ADMIN");

        if (!isOwner && !isAgent && !isAdmin) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to view this user's profile"
            );
        }

        UserResponseDto user = userService.readById(id);

        List<TicketResponseDto> tickets = ticketService.findByCustomerId(id);

        model.addAttribute("user", user);
        model.addAttribute("tickets", tickets);

        if(isAdmin)
            return "admin/user";

        return "user/read";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         Principal principal,
                         Model model){
        String email = principal.getName();

        UserResponseDto user = userService.readByEmail(email);

        if(!user.getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to edit this user's profile"
            );
        }

        model.addAttribute("user", user);
        model.addAttribute("userUpdate", new UserRegistrationDto());

        return "user/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("userUpdate") UserRegistrationDto dto){
        userService.update(id, dto);

        return "redirect:/users/" + id + "/read";
    }
}
