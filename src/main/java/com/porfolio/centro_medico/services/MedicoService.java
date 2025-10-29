package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.RegisterRequest;
import com.porfolio.centro_medico.models.dto.MedicoRequest;
import com.porfolio.centro_medico.models.dto.MedicoResponse;
import com.porfolio.centro_medico.models.dto.UserResponse;
import com.porfolio.centro_medico.models.enums.Especialidad;
import com.porfolio.centro_medico.models.enums.Role;
import com.porfolio.centro_medico.models.mappers.DtoMapper;
import com.porfolio.centro_medico.repositories.MedicoRepository;

@Service
@Transactional
public class MedicoService implements IMedicoService {
    private final MedicoRepository medicoRepository;
    private final UserService userService;
    private final DtoMapper dtoMapper;

    public MedicoService(MedicoRepository medicoRepository, UserService userService, DtoMapper dtoMapper) {
        this.medicoRepository = medicoRepository;
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public MedicoResponse createMedico(MedicoRequest request) {
        if (medicoRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        UserResponse userResponse = userService.createUser(
                new RegisterRequest(
                        request.username(),
                        request.password(),
                        request.email()),
                Role.MEDICO);

        User user = userService.findEntityById(userResponse.id())
                .orElseThrow(() -> new RuntimeException("Error al crear usuario médico"));

        Medico medico = dtoMapper.toEntity(request);
        medico.setUser(user);

        Medico savedMedico = medicoRepository.save(medico);
        return dtoMapper.toResponse(savedMedico);
    }

    @Override
    public List<MedicoResponse> findAll() {
        return medicoRepository.findAll().stream().map(dtoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MedicoResponse> findAllActive() {
        return medicoRepository.findByIsActiveTrue().stream().map(dtoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<MedicoResponse> findById(Long id) {
        return medicoRepository.findById(id).map(dtoMapper::toResponse);
    }

    @Override
    public List<MedicoResponse> findByEspecialidad(Especialidad especialidad) {
        return medicoRepository.findByEspecialidad(especialidad).stream().map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicoResponse updateMedico(Long id, MedicoRequest request) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        dtoMapper.updateMedicoFromRequest(medico, request);
        medico.setUpdatedAt(LocalDateTime.now());

        Medico updatedMedico = medicoRepository.save(medico);
        return dtoMapper.toResponse(updatedMedico);
    }

    @Override
    public void deactivateMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        medico.setIsActive(false);
        medico.setUpdatedAt(LocalDateTime.now());
        medicoRepository.save(medico);
    }

    @Override
    public void activateMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        medico.setIsActive(true);
        medico.setUpdatedAt(LocalDateTime.now());
        medicoRepository.save(medico);
    }

    @Override
    public Optional<Medico> findEntityById(Long id) {
        return medicoRepository.findById(id);
    }

    @Override
    public MedicoResponse getMedicoResponse(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico no encontrado con ID: " + id));
        return dtoMapper.toResponse(medico);
    }

}
