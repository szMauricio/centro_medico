package com.porfolio.centro_medico.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank @Size(min = 5, max = 20) String username,
        @NotBlank @Size(min = 6, max = 15) String password,
        @NotBlank @Email String email) {
}
