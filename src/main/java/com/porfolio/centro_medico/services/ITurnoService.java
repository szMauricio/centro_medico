package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.Turno;
import com.porfolio.centro_medico.models.dto.TurnoRequest;
import com.porfolio.centro_medico.models.dto.TurnoResponse;

public interface ITurnoService {
    Turno createTurno(TurnoRequest request);

    List<Turno> findAll();

    Optional<Turno> findById(Long id);

    List<Turno> findByMedico(Medico medico);

    List<Turno> findByPaciente(Paciente paciente);

    List<Turno> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end);

    List<Turno> findByMedicoAndFechaHoraBetween(Medico medico, LocalDateTime start, LocalDateTime end);

    Turno updateTurno(Long id, TurnoRequest request);

    void cancelarTurno(Long id);

    void completarTurno(Long id);

    TurnoResponse getTurnoResponse(Long id);
}
