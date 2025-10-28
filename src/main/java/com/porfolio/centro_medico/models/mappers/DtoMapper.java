package com.porfolio.centro_medico.models.mappers;

import org.springframework.stereotype.Component;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.Turno;
import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.AuthRequest;
import com.porfolio.centro_medico.models.dto.MedicoRequest;
import com.porfolio.centro_medico.models.dto.MedicoResponse;
import com.porfolio.centro_medico.models.dto.PacienteRequest;
import com.porfolio.centro_medico.models.dto.PacienteResponse;
import com.porfolio.centro_medico.models.dto.TurnoRequest;
import com.porfolio.centro_medico.models.dto.TurnoResponse;
import com.porfolio.centro_medico.models.dto.UserResponse;
import com.porfolio.centro_medico.models.enums.Role;

@Component
public class DtoMapper {
    // ==================== USER MAPPERS ====================

    public User toEntity(AuthRequest request, Role role) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password()); // Se encriptará en el service
        user.setEmail(request.email());
        user.setRole(role);
        return user;
    }

    public UserResponse toResponse(User user) {
        if (user == null)
            return null;

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getIsActive());
    }

    // ==================== PACIENTE MAPPERS ====================

    public Paciente toEntity(PacienteRequest request) {
        Paciente paciente = new Paciente();
        paciente.setDni(request.dni());
        paciente.setNombre(request.nombre());
        paciente.setApellido(request.apellido());
        paciente.setSexo(request.sexo());
        paciente.setFechaNacimiento(request.fechaNacimiento());
        paciente.setDireccion(request.direccion());
        paciente.setEmail(request.email());
        paciente.setTelefono(request.telefono());
        return paciente;
    }

    public PacienteResponse toResponse(Paciente paciente) {
        if (paciente == null)
            return null;

        return new PacienteResponse(
                paciente.getId(),
                paciente.getDni(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getSexo(),
                paciente.getFechaNacimiento(),
                paciente.getDireccion(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getCreatedAt(),
                paciente.getUpdatedAt(),
                toResponse(paciente.getUser()));
    }

    // ==================== MÉDICO MAPPERS ====================

    public Medico toEntity(MedicoRequest request) {
        Medico medico = new Medico();
        medico.setNombre(request.nombre());
        medico.setApellido(request.apellido());
        medico.setEspecialidad(request.especialidad());
        medico.setEmail(request.email());
        medico.setTelefono(request.telefono());
        return medico;
    }

    public MedicoResponse toResponse(Medico medico) {
        if (medico == null)
            return null;

        return new MedicoResponse(
                medico.getId(),
                medico.getNombre(),
                medico.getApellido(),
                medico.getEspecialidad(),
                medico.getEmail(),
                medico.getTelefono(),
                medico.getIsActive(),
                medico.getCreatedAt(),
                medico.getUpdatedAt(),
                toResponse(medico.getUser()));
    }

    // ==================== TURNO MAPPERS ====================

    public Turno toEntity(TurnoRequest request, Paciente paciente, Medico medico) {
        Turno turno = new Turno();
        turno.setFechaHora(request.fechaHora());
        turno.setPaciente(paciente);
        turno.setMedico(medico);
        turno.setObservaciones(request.observaciones());
        return turno;
    }

    public TurnoResponse toResponse(Turno turno) {
        if (turno == null)
            return null;

        return new TurnoResponse(
                turno.getId(),
                turno.getFechaHora(),
                toResponse(turno.getPaciente()),
                toResponse(turno.getMedico()),
                turno.getEstado(),
                turno.getObservaciones(),
                turno.getCreatedAt(),
                turno.getUpdatedAt());
    }

    // ==================== UPDATE MAPPERS ====================

    public void updatePacienteFromRequest(Paciente paciente, PacienteRequest request) {
        if (request.dni() != null)
            paciente.setDni(request.dni());
        if (request.nombre() != null)
            paciente.setNombre(request.nombre());
        if (request.apellido() != null)
            paciente.setApellido(request.apellido());
        if (request.sexo() != null)
            paciente.setSexo(request.sexo());
        if (request.fechaNacimiento() != null)
            paciente.setFechaNacimiento(request.fechaNacimiento());
        if (request.direccion() != null)
            paciente.setDireccion(request.direccion());
        if (request.email() != null)
            paciente.setEmail(request.email());
        if (request.telefono() != null)
            paciente.setTelefono(request.telefono());
    }

    public void updateMedicoFromRequest(Medico medico, MedicoRequest request) {
        if (request.nombre() != null)
            medico.setNombre(request.nombre());
        if (request.apellido() != null)
            medico.setApellido(request.apellido());
        if (request.especialidad() != null)
            medico.setEspecialidad(request.especialidad());
        if (request.email() != null)
            medico.setEmail(request.email());
        if (request.telefono() != null)
            medico.setTelefono(request.telefono());
    }
}
