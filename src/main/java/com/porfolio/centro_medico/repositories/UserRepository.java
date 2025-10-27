package com.porfolio.centro_medico.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.porfolio.centro_medico.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Encuentra usuario por username (para login)
    Optional<User> findByUsername(String username);

    // Encuentra usuario por email
    Optional<User> findByEmail(String email);

    // Verifica si existe un username
    boolean existsByUsername(String username);

    // Verifica si existe un email
    boolean existsByEmail(String email);
}
