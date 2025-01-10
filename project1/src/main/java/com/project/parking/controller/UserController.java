package com.project.parking.controller;

import com.project.parking.entity.User;
import com.project.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Inscription d'un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode le mot de passe
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to register user: " + e.getMessage());
        }
    }

    // 2. Connexion de l'utilisateur (simplifié ici)
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.findByUsername(username);

        // Vérification du mot de passe
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Normalement, ici, tu génèrerais un token JWT ou une session pour authentifier l'utilisateur
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // 3. Récupérer les informations de l'utilisateur
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return "Current user: " + userDetails.getUsername();
    }
}

