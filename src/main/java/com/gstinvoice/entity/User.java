package com.gstinvoice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data

public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Role role;
    private String role;
    
    @Column(nullable = false)
    private LocalDateTime created_at;

    @PrePersist protected void onCreate() { this.created_at = LocalDateTime.now(); }

    // Constructors, getters and setters, and other methods...

    // Getters
    public Long getId() {
        return id;
    }

    public @NotBlank(message = "Username is required") String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getRole() {
        return role;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(@NotBlank(message = "Username is required") String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }


}


