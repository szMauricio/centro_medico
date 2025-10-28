package com.porfolio.centro_medico.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.AuthRequest;
import com.porfolio.centro_medico.models.enums.Role;
import com.porfolio.centro_medico.models.mappers.DtoMapper;
import com.porfolio.centro_medico.repositories.UserRepository;

@Service
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper dtoMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DtoMapper dtoMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public User createUser(AuthRequest request, Role role) {
        if (existsByUsername(request.username())) {
            throw new RuntimeException("El username ya está en uso");
        }

        if (existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está en uso");
        }

        User user = dtoMapper.toEntity(request, role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        Optional<User> userOpt = findByUsername(username);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void toggleUserStatus(Long userId, boolean isActive) {
        User user = findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));
        user.setIsActive(isActive);
        userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
