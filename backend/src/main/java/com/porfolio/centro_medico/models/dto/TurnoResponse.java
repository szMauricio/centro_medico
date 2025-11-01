package com.porfolio.centro_medico.models.dto;

import java.time.LocalDateTime;

import com.porfolio.centro_medico.models.enums.EstadoTurno;

public record TurnoResponse(
        Long id,
        LocalDateTime fechaHora,
        PacienteResponse paciente,
        MedicoResponse medico,
        EstadoTurno estado,
        String observaciones,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
