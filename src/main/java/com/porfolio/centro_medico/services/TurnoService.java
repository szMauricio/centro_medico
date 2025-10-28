package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.Turno;
import com.porfolio.centro_medico.models.dto.TurnoRequest;
import com.porfolio.centro_medico.models.dto.TurnoResponse;
import com.porfolio.centro_medico.models.enums.EstadoTurno;
import com.porfolio.centro_medico.models.mappers.DtoMapper;
import com.porfolio.centro_medico.repositories.TurnoRepository;

@Service
@Transactional
public class TurnoService implements ITurnoService {
    private final TurnoRepository turnoRepository;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final DtoMapper dtoMapper;

    public TurnoService(TurnoRepository turnoRepository, PacienteService pacienteService, MedicoService medicoService,
            DtoMapper dtoMapper) {
        this.turnoRepository = turnoRepository;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public Turno createTurno(TurnoRequest request) {
        if (request.fechaHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden crear turnos en fechas pasadas");
        }

        Paciente paciente = pacienteService.findById(request.pacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoService.findById(request.medicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Validar que no exista un turno en la misma fecha/hora para el mismo médico
        List<Turno> turnosExistentes = turnoRepository.findByMedicoAndFechaHoraBetween(
                medico,
                request.fechaHora().minusMinutes(30),
                request.fechaHora().plusMinutes(30));

        if (!turnosExistentes.isEmpty()) {
            throw new RuntimeException("El médico ya tiene un turno en ese horario");
        }

        Turno turno = dtoMapper.toEntity(request, paciente, medico);
        return turnoRepository.save(turno);
    }

    @Override
    public TurnoResponse getTurnoResponse(Long id) {
        Turno turno = findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
        return dtoMapper.toResponse(turno);
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
    public Turno updateTurno(Long id, TurnoRequest request) {
        Turno turno = findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        // Si se cambia la fecha/hora, validar nuevamente
        if (request.fechaHora() != null && !request.fechaHora().equals(turno.getFechaHora())) {
            if (request.fechaHora().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No se puede cambiar a una fecha pasada");
            }

            // Validar disponibilidad del médico en la nueva fecha
            List<Turno> turnosExistentes = turnoRepository.findByMedicoAndFechaHoraBetween(
                    turno.getMedico(),
                    request.fechaHora().minusMinutes(30),
                    request.fechaHora().plusMinutes(30));

            if (!turnosExistentes.isEmpty()) {
                throw new RuntimeException("El médico ya tiene un turno en ese horario");
            }

            turno.setFechaHora(request.fechaHora());
        }

        if (request.observaciones() != null) {
            turno.setObservaciones(request.observaciones());
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
