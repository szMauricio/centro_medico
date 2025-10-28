package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.PacienteRequest;
import com.porfolio.centro_medico.models.dto.PacienteResponse;
import com.porfolio.centro_medico.models.mappers.DtoMapper;
import com.porfolio.centro_medico.repositories.PacienteRepository;

@Service
@Transactional
public class PacienteService implements IPacienteService {
    private final PacienteRepository pacienteRepository;
    private final UserService userService;
    private final DtoMapper dtoMapper;

    public PacienteService(PacienteRepository pacienteRepository, UserService userService, DtoMapper dtoMapper) {
        this.pacienteRepository = pacienteRepository;
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public PacienteResponse createPaciente(PacienteRequest request) {
        if (existsByDni(request.dni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        User user = userService.findEntityById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = dtoMapper.toEntity(request);
        paciente.setUser(user);

        Paciente savedPaciente = pacienteRepository.save(paciente);
        return dtoMapper.toResponse(savedPaciente);
    }

    @Override
    public List<PacienteResponse> findAll() {
        return pacienteRepository.findAll().stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PacienteResponse> findById(Long id) {
        return pacienteRepository.findById(id)
                .map(dtoMapper::toResponse);
    }

    @Override
    public Optional<PacienteResponse> findByDni(String dni) {
        return pacienteRepository.findByDni(dni)
                .map(dtoMapper::toResponse);
    }

    @Override
    public PacienteResponse updatePaciente(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        dtoMapper.updatePacienteFromRequest(paciente, request);
        paciente.setUpdatedAt(LocalDateTime.now());

        Paciente updatedPaciente = pacienteRepository.save(paciente);
        return dtoMapper.toResponse(updatedPaciente);
    }

    @Override
    public void deletePaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        pacienteRepository.delete(paciente);
    }

    @Override
    public boolean existsByDni(String dni) {
        return pacienteRepository.existsByDni(dni);
    }

    @Override
    public Optional<Paciente> findEntityById(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public PacienteResponse getPacienteResponse(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return dtoMapper.toResponse(paciente);
    }
}
