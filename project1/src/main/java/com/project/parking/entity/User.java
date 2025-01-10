package com.project.parking.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "SP_USER")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role; // Enum définissant les rôles

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
