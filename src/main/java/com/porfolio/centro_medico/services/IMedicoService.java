package com.porfolio.centro_medico.services;

import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.dto.MedicoRequest;
import com.porfolio.centro_medico.models.dto.MedicoResponse;
import com.porfolio.centro_medico.models.enums.Especialidad;

public interface IMedicoService {
    Medico createMedico(MedicoRequest request);

    List<Medico> findAll();

    List<Medico> findAllActive();

    Optional<Medico> findById(Long id);

    List<Medico> findByEspecialidad(Especialidad especialidad);

    Medico updateMedico(Long id, MedicoRequest request);

    void deactivateMedico(Long id);

    void activateMedico(Long id);

    MedicoResponse getMedicoResponse(Long id);
}
