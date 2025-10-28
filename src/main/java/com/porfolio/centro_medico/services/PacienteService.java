package com.porfolio.centro_medico.services;

import java.util.List;
import java.util.Optional;

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
    public Paciente createPaciente(PacienteRequest request) {
        if (pacienteRepository.existsByDni(request.dni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        User user = userService.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = dtoMapper.toEntity(request);
        paciente.setUser(user);

        return pacienteRepository.save(paciente);
    }

    @Override
    public PacienteResponse getPacienteResponse(Long id) {
        Paciente paciente = findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return dtoMapper.toResponse(paciente);
    }

    @Override
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Optional<Paciente> findByDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    @Override
    public Paciente updatePaciente(Long id, PacienteRequest request) {
        Paciente paciente = findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));

        dtoMapper.updatePacienteFromRequest(paciente, request);
        paciente.setUpdatedAt(java.time.LocalDateTime.now());

        return pacienteRepository.save(paciente);
    }

    @Override
    public void deletePaciente(Long id) {
        Paciente paciente = findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));
        pacienteRepository.delete(paciente);
    }

    @Override
    public boolean existsByDni(String dni) {
        return pacienteRepository.existsByDni(dni);
    }

}
