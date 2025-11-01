package com.porfolio.centro_medico.models.dto;

import java.time.LocalDate;

import com.porfolio.centro_medico.models.enums.Sexo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record PacienteRequest(
        @NotBlank @Size(min = 7, max = 10) String dni,
        @NotBlank @Size(max = 20) String nombre,
        @NotBlank @Size(max = 50) String apellido,
        @NotNull Sexo sexo,
        @NotNull @Past LocalDate fechaNacimiento,
        String direccion,
        String email,
        String telefono,
        @NotNull Long userId) {

}
