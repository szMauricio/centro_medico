package com.porfolio.centro_medico.services;

import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Paciente;

public interface IPacienteService {
    Paciente createPaciente(Paciente paciente);

    List<Paciente> findAll();

    Optional<Paciente> findById(Long id);

    Optional<Paciente> findByDni(String dni);

    Paciente updatePaciente(Long id, Paciente details);

    void deletePaciente(Long id);

    boolean existsByDni(String dni);
}
