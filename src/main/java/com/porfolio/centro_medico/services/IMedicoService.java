package com.porfolio.centro_medico.services;

import java.util.List;
import java.util.Optional;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.enums.Especialidad;

public interface IMedicoService {
    Medico createMedico(Medico medico);

    List<Medico> findAll();

    List<Medico> findAllActive();

    Optional<Medico> findById(Long id);

    List<Medico> findByEspecialidad(Especialidad especialidad);

    Medico updateMedico(Long id, Medico details);

    void deactivateMedico(Long id);

    void activateMedico(Long id);
}
