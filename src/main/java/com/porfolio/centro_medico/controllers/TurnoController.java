package com.porfolio.centro_medico.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.porfolio.centro_medico.models.dto.TurnoRequest;
import com.porfolio.centro_medico.models.dto.TurnoResponse;
import com.porfolio.centro_medico.services.SecurityService;
import com.porfolio.centro_medico.services.TurnoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private final TurnoService turnoService;
    private final SecurityService securityService;

    public TurnoController(TurnoService turnoService, SecurityService securityService) {
        this.turnoService = turnoService;
        this.securityService = securityService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TurnoResponse>> getAllTurnos() {
        return ResponseEntity.ok(turnoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponse> getTurnoById(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.getTurnoResponse(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createTurno(@Valid @RequestBody TurnoRequest request) {
        try {
            // Validar que el pacienteId del request pertenece al usuario logueado
            if (!securityService.isOwnerOfPaciente(request.pacienteId()) && !securityService.isAdmin()) {
                return ResponseEntity.status(403).body("No tienes permisos para crear turnos para este paciente");
            }

            TurnoResponse turno = turnoService.createTurno(request);
            return ResponseEntity.ok(turno);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTurno(@PathVariable Long id, @Valid @RequestBody TurnoRequest request) {
        try {
            // Validar permisos - solo admin o dueño del turno puede actualizar
            if (!securityService.isAdmin() && !isOwnerOfTurno(id)) {
                return ResponseEntity.status(403).body("No tienes permisos para actualizar este turno");
            }

            TurnoResponse turno = turnoService.updateTurno(id, request);
            return ResponseEntity.ok(turno);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarTurno(@PathVariable Long id) {
        try {
            // Validar que el usuario tiene permisos para cancelar este turno
            if (!securityService.isAdmin() && !isOwnerOfTurno(id)) {
                return ResponseEntity.status(403).body("No tienes permisos para cancelar este turno");
            }

            turnoService.cancelarTurno(id);
            return ResponseEntity.ok("Turno cancelado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<?> completarTurno(@PathVariable Long id) {
        try {
            // Validar que el médico tiene permisos para completar este turno
            if (!securityService.isAdmin() && !isMedicoOfTurno(id)) {
                return ResponseEntity.status(403).body("No tienes permisos para completar este turno");
            }

            turnoService.completarTurno(id);
            return ResponseEntity.ok("Turno completado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<TurnoResponse>> getTurnosByMedico(@PathVariable Long medicoId) {
        List<TurnoResponse> turnos = turnoService.findByMedicoId(medicoId);
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<TurnoResponse>> getTurnosByPaciente(@PathVariable Long pacienteId) {
        List<TurnoResponse> turnos = turnoService.findByPacienteId(pacienteId);
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/rango")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TurnoResponse>> getTurnosByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TurnoResponse> turnos = turnoService.findByFechaHoraBetween(start, end);
        return ResponseEntity.ok(turnos);
    }

    // Métodos auxiliares para verificar propiedad
    private boolean isOwnerOfTurno(Long turnoId) {
        return turnoService.findByEntityId(turnoId)
                .map(turno -> {
                    Long currentUserId = securityService.getCurrentUserId();
                    // Acceder a las relaciones a través de las Entities, no DTOs
                    return turno.getPaciente().getUser().getId().equals(currentUserId);
                })
                .orElse(false);
    }

    private boolean isMedicoOfTurno(Long turnoId) {
        return turnoService.findByEntityId(turnoId)
                .map(turno -> {
                    Long currentUserId = securityService.getCurrentUserId();
                    // Acceder a las relaciones a través de las Entities, no DTOs
                    return turno.getMedico().getUser().getId().equals(currentUserId);
                })
                .orElse(false);
    }
}
