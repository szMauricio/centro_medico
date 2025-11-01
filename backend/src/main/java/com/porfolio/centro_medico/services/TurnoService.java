package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.exceptions.BusinessException;
import com.porfolio.centro_medico.exceptions.ResourceNotFoundException;
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
    public TurnoResponse createTurno(TurnoRequest request) {
        if (request.fechaHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("No se pueden crear turnos en fechas pasadas");
        }

        Paciente paciente = pacienteService.findEntityById(request.pacienteId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Paciente no encontrado con ID:" + request.pacienteId()));

        Medico medico = medicoService.findEntityById(request.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + request.medicoId()));

        // Validar que no exista un turno en la misma fecha/hora para el mismo médico
        List<Turno> turnosExistentes = turnoRepository.findByMedicoAndFechaHoraBetween(
                medico,
                request.fechaHora().minusMinutes(30),
                request.fechaHora().plusMinutes(30));

        if (!turnosExistentes.isEmpty()) {
            throw new BusinessException("El médico ya tiene un turno en ese horario");
        }

        Turno turno = dtoMapper.toEntity(request, paciente, medico);

        Turno savedTurno = turnoRepository.save(turno);
        return dtoMapper.toResponse(savedTurno);
    }

    @Override
    public List<TurnoResponse> findAll() {
        return turnoRepository.findAll().stream().map(dtoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<TurnoResponse> findById(Long id) {
        return turnoRepository.findById(id).map(dtoMapper::toResponse);
    }

    @Override
    public List<TurnoResponse> findByMedico(Medico medico) {
        return turnoRepository.findByMedico(medico).stream().map(dtoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TurnoResponse> findByPaciente(Paciente paciente) {
        return turnoRepository.findByPaciente(paciente).stream().map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TurnoResponse> findByFechaHoraBetween(LocalDateTime start, LocalDateTime end) {
        return turnoRepository.findByFechaHoraBetween(start, end).stream().map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TurnoResponse> findByMedicoAndFechaHoraBetween(Medico medico, LocalDateTime start, LocalDateTime end) {
        return turnoRepository.findByMedicoAndFechaHoraBetween(medico, start, end).stream().map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TurnoResponse updateTurno(Long id, TurnoRequest request) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con ID: " + id));

        // Si se cambia la fecha/hora, validar nuevamente
        if (request.fechaHora() != null && !request.fechaHora().equals(turno.getFechaHora())) {
            if (request.fechaHora().isBefore(LocalDateTime.now())) {
                throw new BusinessException("No se puede cambiar a una fecha pasada");
            }

            // Validar disponibilidad del médico en la nueva fecha
            List<Turno> turnosExistentes = turnoRepository.findByMedicoAndFechaHoraBetween(
                    turno.getMedico(),
                    request.fechaHora().minusMinutes(30),
                    request.fechaHora().plusMinutes(30));

            if (!turnosExistentes.isEmpty()) {
                throw new BusinessException("El médico ya tiene un turno en ese horario");
            }

            turno.setFechaHora(request.fechaHora());
        }

        if (request.observaciones() != null) {
            turno.setObservaciones(request.observaciones());
        }

        turno.setUpdatedAt(LocalDateTime.now());

        Turno updatedTurno = turnoRepository.save(turno);
        return dtoMapper.toResponse(updatedTurno);
    }

    @Override
    public void cancelarTurno(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con ID: " + id));

        if (turno.getEstado() == EstadoTurno.COMPLETADO) {
            throw new BusinessException("No se puede cancelar un turno completado");
        }

        turno.setEstado(EstadoTurno.CANCELADO);
        turno.setUpdatedAt(LocalDateTime.now());
        turnoRepository.save(turno);
    }

    @Override
    public void completarTurno(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con ID: " + id));

        if (turno.getEstado() == EstadoTurno.CANCELADO) {
            throw new BusinessException("No se puede completar un turno cancelado");
        }

        turno.setEstado(EstadoTurno.COMPLETADO);
        turno.setUpdatedAt(LocalDateTime.now());
        turnoRepository.save(turno);
    }

    @Override
    public TurnoResponse getTurnoResponse(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado con ID: " + id));
        return dtoMapper.toResponse(turno);
    }

    @Override
    public List<TurnoResponse> findByMedicoId(Long medicoId) {
        Medico medico = medicoService.findEntityById(medicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + medicoId));

        return turnoRepository.findByMedico(medico).stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TurnoResponse> findByPacienteId(Long pacienteId) {
        Paciente paciente = pacienteService.findEntityById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + pacienteId));

        return turnoRepository.findByPaciente(paciente).stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Turno> findByEntityId(Long id) {
        return turnoRepository.findById(id);
    }

}
