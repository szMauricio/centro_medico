package com.porfolio.centro_medico.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.porfolio.centro_medico.models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Encuentra paciente por DNI
    Optional<Paciente> findByDni(String dni);

    // Encuentra paciente por email
    Optional<Paciente> findByEmail(String email);

    // Verifica si existe un DNI
    boolean existsByDni(String dni);
}
