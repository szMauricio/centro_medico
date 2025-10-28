package com.porfolio.centro_medico.services;

import java.util.Optional;

import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.RegisterRequest;
import com.porfolio.centro_medico.models.dto.UserResponse;
import com.porfolio.centro_medico.models.enums.Role;

public interface IUserService {
    UserResponse createUser(RegisterRequest request, Role role);

    Optional<UserResponse> findByUsername(String username);

    Optional<UserResponse> findById(Long id);

    boolean validateCredentials(String username, String password);

    void toggleUserStatus(Long userId, boolean isActive);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findEntityById(Long id);

    Optional<User> findEntityByUsername(String username);
}
