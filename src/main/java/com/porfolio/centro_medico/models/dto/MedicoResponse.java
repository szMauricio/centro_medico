package com.porfolio.centro_medico.models.dto;

import com.porfolio.centro_medico.models.enums.Especialidad;

public record MedicoResponse(
        Long id,
        String nombre,
        String apellido,
        Especialidad especialidad,
        String email,
        String telefono,
        Boolean isActive,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt,
        UserResponse user) {

}
