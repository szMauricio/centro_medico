package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.AuthRequest;
import com.porfolio.centro_medico.models.dto.MedicoRequest;
import com.porfolio.centro_medico.models.dto.MedicoResponse;
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
    public Medico createMedico(MedicoRequest request) {
        if (medicoRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = userService.createUser(
                new AuthRequest(
                        request.username(),
                        request.password(),
                        request.email()),
                Role.ROLE_MEDICO);

        Medico medico = dtoMapper.toEntity(request);
        medico.setUser(user);

        return medicoRepository.save(medico);
    }

    @Override
    public MedicoResponse getMedicoResponse(Long id) {
        Medico medico = findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        return dtoMapper.toResponse(medico);
    }

    @Override
    public List<Medico> findAll() {
        return medicoRepository.findAll();
    }

    @Override
    public List<Medico> findAllActive() {
        return medicoRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Medico> findById(Long id) {
        return medicoRepository.findById(id);
    }

    @Override
    public List<Medico> findByEspecialidad(Especialidad especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

    @Override
    public Medico updateMedico(Long id, MedicoRequest request) {
        Medico medico = findById(id).orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        dtoMapper.updateMedicoFromRequest(medico, request);
        medico.setUpdatedAt(LocalDateTime.now());

        return medicoRepository.save(medico);
    }

    @Override
    public void deactivateMedico(Long id) {
        Medico medico = findById(id).orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        medico.setIsActive(false);
        medico.setUpdatedAt(LocalDateTime.now());
        medicoRepository.save(medico);
    }

    @Override
    public void activateMedico(Long id) {
        Medico medico = findById(id).orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        medico.setIsActive(true);
        medico.setUpdatedAt(LocalDateTime.now());
        medicoRepository.save(medico);
    }

}
