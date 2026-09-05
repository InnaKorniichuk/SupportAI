package com.supportai.app.controller;

import com.supportai.app.dto.message.MessageCreateDto;
import com.supportai.app.dto.user.UserResponseDto;
import com.supportai.app.service.AiService;
import com.supportai.app.service.MessageService;
import com.supportai.app.service.TicketService;
import com.supportai.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users/{userId}/tickets/{ticketId}/messages")
public class MessageController {
    private final MessageService messageService;
    private final TicketService ticketService;
    private final UserService userService;
    private final AiService aiService;

    public MessageController(MessageService messageService,
                             TicketService ticketService,
                             UserService userService,
                             AiService aiService){
        this.messageService = messageService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.aiService = aiService;
    }

    @PostMapping("/create")
    public String create(
            @PathVariable Long userId,
            @PathVariable Long ticketId,
            @ModelAttribute("message") MessageCreateDto messageDto,
            Principal principal) {

        UserResponseDto currentUser =
                userService.readByEmail(principal.getName());

        messageService.create(
                messageDto,
                currentUser.getId(),
                ticketId
        );

        return "redirect:/users/" + userId
                + "/tickets/" + ticketId + "/read";
    }

    @PostMapping("/generate-ai")
    public String generateAiResponse(
            @PathVariable Long userId,
            @PathVariable Long ticketId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        String aiResponse =
                aiService.generateResponse(ticketId);

        redirectAttributes.addFlashAttribute(
                "aiResponse",
                aiResponse
        );

        return "redirect:/users/"
                + userId
                + "/tickets/"
                + ticketId
                + "/read";
    }
}
