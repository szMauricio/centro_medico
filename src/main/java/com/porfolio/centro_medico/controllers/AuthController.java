package com.porfolio.centro_medico.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porfolio.centro_medico.config.JwtUtil;
import com.porfolio.centro_medico.models.dto.LoginRequest;
import com.porfolio.centro_medico.models.dto.RegisterRequest;
import com.porfolio.centro_medico.models.dto.RegisterResponse;
import com.porfolio.centro_medico.models.dto.UserResponse;
import com.porfolio.centro_medico.models.enums.Role;
import com.porfolio.centro_medico.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse userResponse = userService.createUser(request, Role.USER);

            // Generar JWT después del registro
            String token = jwtUtil.generateToken(
                    userResponse.username(),
                    userResponse.id(),
                    userResponse.role().name());

            RegisterResponse registerResponse = new RegisterResponse(token, userResponse);
            return ResponseEntity.ok(registerResponse);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            boolean isValid = userService.validateCredentials(request.username(), request.password());

            if (!isValid) {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }

            var userOpt = userService.findByUsername(request.username());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Usuario no encontrado");
            }

            UserResponse userResponse = userOpt.get();

            // Generar JWT
            String token = jwtUtil.generateToken(
                    userResponse.username(),
                    userResponse.id(),
                    userResponse.role().name());

            RegisterResponse registerResponse = new RegisterResponse(token, userResponse);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en login: " + e.getMessage());
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(!exists);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(!exists);
    }
}
