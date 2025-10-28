package com.porfolio.centro_medico.services;

import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.dto.PacienteRequest;
import com.porfolio.centro_medico.models.dto.PacienteResponse;

public interface IPacienteService {
    Paciente createPaciente(PacienteRequest request);

    List<Paciente> findAll();

    Optional<Paciente> findById(Long id);

    Optional<Paciente> findByDni(String dni);

    Paciente updatePaciente(Long id, PacienteRequest request);

    void deletePaciente(Long id);

    boolean existsByDni(String dni);

    PacienteResponse getPacienteResponse(Long id);
}
