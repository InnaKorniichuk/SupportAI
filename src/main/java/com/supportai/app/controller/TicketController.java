package com.supportai.app.controller;

import com.supportai.app.dto.message.MessageCreateDto;
import com.supportai.app.dto.ticket.TicketCreateDto;
import com.supportai.app.dto.ticket.TicketDetailsDto;
import com.supportai.app.dto.ticket.TicketResponseDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.model.Ticket;
import com.supportai.app.service.TicketService;
import com.supportai.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/users/{userId}/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(TicketService ticketService,
                            UserService userService){
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String create(@PathVariable("userId") Long userId,
                         Model model) {

        model.addAttribute("ticket", new TicketCreateDto());
        model.addAttribute("id", userId);
        model.addAttribute("agents", userService.findAgents());

        return "ticket/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("ticket") TicketCreateDto ticketDto,
                         @PathVariable("userId") Long userId,
                         @RequestParam Long agentId) {

        Ticket ticket = ticketService.create(
                ticketDto,
                userId,
                agentId
        );

        return "redirect:/users/" + userId + "/read";
    }

    @GetMapping("/{ticketId}/read")
    public String read(
            @PathVariable Long userId,
            @PathVariable Long ticketId,
            Model model,
            Principal principal) {

        String email = principal.getName();

        UserResponseDto currentUser = userService.readByEmail(email);

        TicketDetailsDto ticket = ticketService.getDetails(ticketId);

        if (!ticket.getCustomer().getId().equals(currentUser.getId())
                && !currentUser.getRole().equals("AGENT")
                && !currentUser.getRole().equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to view this ticket"
            );
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("message", new MessageCreateDto());

        return "ticket/read";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @PathVariable Long userId,
                         Principal principal){
        String email = principal.getName();

        UserResponseDto currentUser = userService.readByEmail(email);

        TicketResponseDto ticketResponseDto = ticketService.readById(id);
        if (!ticketResponseDto.getCustomer().getId().equals(currentUser.getId())
                && !currentUser.getRole().equals("AGENT")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to delete this ticket"
            );
        }

        ticketService.delete(id);

        return "redirect:/users/" + userId +"/read";
    }
}
