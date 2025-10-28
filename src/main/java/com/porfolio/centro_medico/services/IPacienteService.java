package com.porfolio.centro_medico.services;

import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.dto.PacienteRequest;
import com.porfolio.centro_medico.models.dto.PacienteResponse;

public interface IPacienteService {
    PacienteResponse createPaciente(PacienteRequest request);

    List<PacienteResponse> findAll();

    Optional<PacienteResponse> findById(Long id);

    Optional<PacienteResponse> findByDni(String dni);

    PacienteResponse updatePaciente(Long id, PacienteRequest request);

    void deletePaciente(Long id);

    boolean existsByDni(String dni);

    Optional<Paciente> findEntityById(Long id);
}
