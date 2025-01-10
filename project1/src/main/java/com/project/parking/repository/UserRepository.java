package com.project.parking.repository;

import com.project.parking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // Rechercher un utilisateur par nom d'utilisateur
}
