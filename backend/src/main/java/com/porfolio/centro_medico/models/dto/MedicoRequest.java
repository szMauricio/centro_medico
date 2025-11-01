package com.porfolio.centro_medico.models.dto;

import com.porfolio.centro_medico.models.enums.Especialidad;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicoRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(max = 50) String apellido,
        @NotNull Especialidad especialidad,
        @NotBlank @Email String email,
        String telefono,
        @NotBlank String username,
        @NotBlank @Size(min = 6) String password) {

}
