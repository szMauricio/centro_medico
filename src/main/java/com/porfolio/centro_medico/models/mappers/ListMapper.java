package com.porfolio.centro_medico.models.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.porfolio.centro_medico.models.Medico;
import com.porfolio.centro_medico.models.Paciente;
import com.porfolio.centro_medico.models.Turno;
import com.porfolio.centro_medico.models.User;
import com.porfolio.centro_medico.models.dto.MedicoResponse;
import com.porfolio.centro_medico.models.dto.PacienteResponse;
import com.porfolio.centro_medico.models.dto.TurnoResponse;
import com.porfolio.centro_medico.models.dto.UserResponse;

@Component
public class ListMapper {
    private final DtoMapper dtoMapper;

    public ListMapper(DtoMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    public List<PacienteResponse> toPacienteResponseList(List<Paciente> pacientes) {
        return pacientes.stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<MedicoResponse> toMedicoResponseList(List<Medico> medicos) {
        return medicos.stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TurnoResponse> toTurnoResponseList(List<Turno> turnos) {
        return turnos.stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
