package com.porfolio.centro_medico.services;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.porfolio.centro_medico.models.User;

@Service
public class SecurityService {
    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userService.findEntityByUsername(username);
        }
        return Optional.empty();
    }

    // Obtener ID del usuario actual
    public Long getCurrentUserId() {
        return getCurrentUser()
                .map(User::getId)
                .orElse(null);
    }

    // Verificar si el usuario actual es el dueño del paciente
    public boolean isOwnerOfPaciente(Long pacienteId) {
        return getCurrentUser()
                .map(user -> {
                    // Verificar si el usuario tiene un paciente y si coincide con el ID
                    return user.getPaciente() != null &&
                            user.getPaciente().getId().equals(pacienteId);
                })
                .orElse(false);
    }

    // Verificar si el usuario actual es el dueño del médico
    public boolean isOwnerOfMedico(Long medicoId) {
        return getCurrentUser()
                .map(user -> {
                    // Verificar si el usuario tiene un médico y si coincide con el ID
                    return user.getMedico() != null &&
                            user.getMedico().getId().equals(medicoId);
                })
                .orElse(false);
    }

    // Verificar si el usuario actual es ADMIN
    public boolean isAdmin() {
        return getCurrentUser()
                .map(user -> user.getRole().name().equals("ADMIN"))
                .orElse(false);
    }
}
