package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.enums.Especialidad;
import com.porfolio.centro_medico.repositories.MedicoRepository;

@Service
@Transactional
public class MedicoService implements IMedicoService {
    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Override
    public Medico createMedico(Medico medico) {
        if (medicoRepository.findByEmail(medico.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        return medicoRepository.save(medico);
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
    public Medico updateMedico(Long id, Medico details) {
        Medico medico = findById(id).orElseThrow(() -> new RuntimeException("Médico no encontrado con id: " + id));

        if (details.getTelefono() != null) {
            medico.setTelefono(details.getTelefono());
        }
        if (details.getIsActive() != null) {
            medico.setIsActive(details.getIsActive());
        }
        if (details.getEspecialidad() != null) {
            medico.setEspecialidad(details.getEspecialidad());
        }

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
