package com.porfolio.centro_medico.services;

import java.util.Optional;

import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.AuthRequest;
import com.porfolio.centro_medico.models.enums.Role;

public interface IUserService {
    User createUser(AuthRequest request, Role role);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    boolean validateCredentials(String username, String password);

    void toggleUserStatus(Long userId, boolean isActive);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
