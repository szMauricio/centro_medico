package com.porfolio.centro_medico.models.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record TurnoRequest(
        @NotNull @Future LocalDateTime fechaHora,
        @NotNull Long pacienteId,
        @NotNull Long medicoId,
        String observaciones) {

}
