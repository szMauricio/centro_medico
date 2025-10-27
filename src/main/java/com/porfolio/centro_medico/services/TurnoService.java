package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.Turno;
import com.porfolio.centro_medico.models.enums.EstadoTurno;
import com.porfolio.centro_medico.repositories.TurnoRepository;

@Service
@Transactional
public class TurnoService implements ITurnoService {
    private final TurnoRepository turnoRepository;

    public TurnoService(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    public Turno createTurno(Turno turno) {
        if (turno.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden crear turnos en fechas pasadas");
        }

        List<Turno> turnosExistentes = turnoRepository.findByMedicoAndFechaHoraBetween(
                turno.getMedico(),
                turno.getFechaHora().minusMinutes(30),
                turno.getFechaHora().plusMinutes(30));

        if (!turnosExistentes.isEmpty()) {
            throw new RuntimeException("El médico ya tiene un turno en ese horario");
        }

        return turnoRepository.save(turno);
    }

    @Override
    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    @Override
    public Optional<Turno> findById(Long id) {
        return turnoRepository.findById(id);
    }

    @Override
    public List<Turno> findByMedico(Medico medico) {
        return turnoRepository.findByMedico(medico);
    }

    @Override
    public List<Turno> findByPaciente(Paciente paciente) {
        return turnoRepository.findByPaciente(paciente);
    }

    @Override
    public List<Turno> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end) {
        return turnoRepository.findByFechaHoraBetween(start, end);
    }

    @Override
    public List<Turno> findByMedicoAndFechaHoraBetween(Medico medico, LocalDateTime start, LocalDateTime end) {
        return turnoRepository.findByMedicoAndFechaHoraBetween(medico, start, end);
    }

    @Override
    public Turno updateTurno(Long id, Turno turnoDetails) {
        Turno turno = findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));

        if (turnoDetails.getObservaciones() != null) {
            turno.setObservaciones(turnoDetails.getObservaciones());
        }
        if (turnoDetails.getEstado() != null) {
            turno.setEstado(turnoDetails.getEstado());
        }

        turno.setUpdatedAt(LocalDateTime.now());
        return turnoRepository.save(turno);
    }

    @Override
    public void cancelarTurno(Long id) {
        Turno turno = findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));

        if (turno.getEstado() == EstadoTurno.COMPLETADO) {
            throw new RuntimeException("No se puede cancelar un turno completado");
        }

        turno.setEstado(EstadoTurno.CANCELADO);
        turno.setUpdatedAt(LocalDateTime.now());
        turnoRepository.save(turno);
    }

    @Override
    public void completarTurno(Long id) {
        Turno turno = findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));

        if (turno.getEstado() == EstadoTurno.CANCELADO) {
            throw new RuntimeException("No se puede completar un turno cancelado");
        }

        turno.setEstado(EstadoTurno.COMPLETADO);
        turno.setUpdatedAt(LocalDateTime.now());
        turnoRepository.save(turno);
    }

}
