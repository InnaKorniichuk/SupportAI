package com.supportai.app.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToOne(optional = false)
    private User sender;

    @ManyToOne(optional = false)
    private Ticket ticket;

    private LocalDateTime sentAt;
}
