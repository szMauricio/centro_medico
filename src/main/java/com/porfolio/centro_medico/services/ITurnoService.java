package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.dto.TurnoRequest;
import com.porfolio.centro_medico.models.dto.TurnoResponse;

public interface ITurnoService {
    TurnoResponse createTurno(TurnoRequest request);

    List<TurnoResponse> findAll();

    Optional<TurnoResponse> findById(Long id);

    List<TurnoResponse> findByMedico(Medico medico);

    List<TurnoResponse> findByPaciente(Paciente paciente);

    List<TurnoResponse> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end);

    List<TurnoResponse> findByMedicoAndFechaHoraBetween(Medico medico, LocalDateTime start, LocalDateTime end);

    TurnoResponse updateTurno(Long id, TurnoRequest request);

    void cancelarTurno(Long id);

    void completarTurno(Long id);
}
