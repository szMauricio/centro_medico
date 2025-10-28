package com.porfolio.centro_medico.models.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username es obligatorio") String username,
        @NotBlank(message = "Password es obligatorio") String password) {

}
