package com.porfolio.centro_medico.models.enums;

public enum EstadoTurno {
    PENDIENTE, // Recién agendado
    CONFIRMADO, // Paciente confirmó
    COMPLETADO, // Finalizado exitosamente
    CANCELADO, // Cancelado por paciente/médico
    NO_ASISTIO // Paciente no se presentó
}
