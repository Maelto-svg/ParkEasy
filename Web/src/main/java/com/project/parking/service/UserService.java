package com.project.parking.service;

import com.project.parking.entity.User;
import com.project.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required=true)
    private PasswordEncoder passwordEncoder; // Si on utilise Spring Security

    // Inscrire un nouvel utilisateur
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Chiffre le mot de passe
        return userRepository.save(user);
    }

    // Rechercher un utilisateur par son nom d'utilisateur
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
