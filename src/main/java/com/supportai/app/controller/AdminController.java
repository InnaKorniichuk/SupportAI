package com.supportai.app.controller;

import com.supportai.app.dto.ticket.TicketResponseDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.TicketStatus;
import com.supportai.app.service.TicketService;
import com.supportai.app.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final TicketService ticketService;

    public AdminController(UserService userService,
                           TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<UserResponseDto> users = userService.findAll();

        model.addAttribute("totalUsers", users.size());

        model.addAttribute(
                "customers",
                users.stream()
                        .filter(user -> user.getRole().equals("CUSTOMER"))
                        .count()
        );

        model.addAttribute(
                "agents",
                users.stream()
                        .filter(user -> user.getRole().equals("AGENT"))
                        .count()
        );

        model.addAttribute(
                "admins",
                users.stream()
                        .filter(user -> user.getRole().equals("ADMIN"))
                        .count()
        );

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {

        model.addAttribute("users", userService.findAll());

        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            Principal principal) {

        userService.delete(id, principal.getName());

        return "redirect:/admin/users";
    }

    @GetMapping("/tickets")
    public String tickets(Model model) {

        List<TicketResponseDto> tickets = ticketService.findAll();

        model.addAttribute("tickets", tickets);

        return "admin/tickets";
    }

    @PostMapping("/ticket/{id}/changestatus")
    public String changeTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status) {

        ticketService.changeStatus(id, status);

        return "redirect:/admin/tickets";
    }
}