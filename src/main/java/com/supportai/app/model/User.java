package com.supportai.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "customers")
public class User {
    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Pattern(
            regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}",
            message = "Must be a valid email address")
    @Column(nullable = false,unique = true)
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain upper, lower case letters and a digit"
    )
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
