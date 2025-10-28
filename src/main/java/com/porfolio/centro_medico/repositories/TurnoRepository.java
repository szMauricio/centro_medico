package com.porfolio.centro_medico.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.Turno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    // Encuentra turnos por médico
    List<Turno> findByMedico(Medico medico);

    // Encuentra turnos por paciente
    List<Turno> findByPaciente(Paciente paciente);

    // Encuentra turnos en un rango de fechas
    List<Turno> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);

    // Encuentra turnos por médico y fecha
    List<Turno> findByMedicoAndFechaHoraBetween(Medico medico, LocalDateTime desde, LocalDateTime hasta);
}
