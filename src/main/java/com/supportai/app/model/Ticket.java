package com.supportai.app.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Ticket {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 350)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus ticketStatus;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    private User customer;

    @ManyToOne
    private User assignedAgent;
}
