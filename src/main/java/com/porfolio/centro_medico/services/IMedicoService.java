package com.porfolio.centro_medico.services;

import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.dto.MedicoRequest;
import com.porfolio.centro_medico.models.dto.MedicoResponse;
import com.porfolio.centro_medico.models.enums.Especialidad;

public interface IMedicoService {
    MedicoResponse createMedico(MedicoRequest request);

    List<MedicoResponse> findAll();

    List<MedicoResponse> findAllActive();

    Optional<MedicoResponse> findById(Long id);

    List<MedicoResponse> findByEspecialidad(Especialidad especialidad);

    MedicoResponse updateMedico(Long id, MedicoRequest request);

    void deactivateMedico(Long id);

    void activateMedico(Long id);

    Optional<Medico> findEntityById(Long id);

    MedicoResponse getMedicoResponse(Long id);
}
