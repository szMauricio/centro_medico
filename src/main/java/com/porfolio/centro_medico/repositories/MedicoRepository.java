package com.porfolio.centro_medico.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.enums.Especialidad;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // Encuentra médicos por especialidad
    List<Medico> findByEspecialidad(Especialidad especialidad);

    // Encuentra médico por email
    Optional<Medico> findByEmail(String email);

    // Encuentra médicos activos
    List<Medico> findByIsActiveTrue();
}
