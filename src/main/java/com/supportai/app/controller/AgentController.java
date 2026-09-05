package com.supportai.app.controller;

import com.supportai.app.dto.ticket.TicketResponseDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.service.TicketService;
import com.supportai.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/agent")
public class AgentController {

    private final UserService userService;
    private final TicketService ticketService;

    public AgentController(UserService userService,
                           TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping("/dashboard")
    public String service(Model model, Principal principal) {

        String email = principal.getName();

        UserResponseDto agent = userService.readByEmail(email);

        List<TicketResponseDto> tickets =
                ticketService.findByAssignedAgentId(agent.getId());

        model.addAttribute("user", agent);
        model.addAttribute("tickets", tickets);

        return "agent/dashboard";
    }
}
