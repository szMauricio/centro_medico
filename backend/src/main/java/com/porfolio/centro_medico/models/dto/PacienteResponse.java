package com.porfolio.centro_medico.models.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.porfolio.centro_medico.models.enums.Sexo;

public record PacienteResponse(
        Long id,
        String dni,
        String nombre,
        String apellido,
        Sexo sexo,
        LocalDate fechaNacimiento,
        String direccion,
        String email,
        String telefono,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserResponse user) {

}
