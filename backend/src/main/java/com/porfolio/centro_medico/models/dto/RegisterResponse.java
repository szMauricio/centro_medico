package com.porfolio.centro_medico.models.dto;

public record RegisterResponse(
        String token,
        String type,
        UserResponse user) {

    public RegisterResponse(String token, UserResponse user) {
        this(token, "Bearer", user);
    }
}
