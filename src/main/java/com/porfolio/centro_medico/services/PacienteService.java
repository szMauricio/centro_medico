package com.porfolio.centro_medico.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.repositories.PacienteRepository;

@Service
@Transactional
public class PacienteService implements IPacienteService {
    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente createPaciente(Paciente paciente) {
        if (existsByDni(paciente.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        return pacienteRepository.save(paciente);
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
    public Paciente updatePaciente(Long id, Paciente details) {
        Paciente paciente = findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));

        if (details.getNombre() != null) {
            paciente.setNombre(details.getNombre());
        }
        if (details.getApellido() != null) {
            paciente.setApellido(details.getApellido());
        }
        if (details.getDireccion() != null) {
            paciente.setDireccion(details.getDireccion());
        }
        if (details.getTelefono() != null) {
            paciente.setTelefono(details.getTelefono());
        }

        paciente.setUpdatedAt(LocalDateTime.now());
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
