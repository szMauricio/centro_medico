package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.Turno;

public interface ITurnoService {
    Turno createTurno(Turno turno);

    List<Turno> findAll();

    Optional<Turno> findById(Long id);

    List<Turno> findByMedico(Medico medico);

    List<Turno> findByPaciente(Paciente paciente);

    List<Turno> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end);

    List<Turno> findByMedicoAndFechaHoraBetween(Medico medico, LocalDateTime start, LocalDateTime end);

    Turno updateTurno(Long id, Turno turnoDetails);

    void cancelarTurno(Long id);

    void completarTurno(Long id);
}
