package com.porfolio.centro_medico.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porfolio.centro_medico.models.dto.PacienteRequest;
import com.porfolio.centro_medico.models.dto.PacienteResponse;
import com.porfolio.centro_medico.services.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PacienteResponse>> getAllPacientes() {
        return ResponseEntity.ok(pacienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> getPacienteById(@PathVariable Long id) {
        PacienteResponse paciente = pacienteService.getPacienteResponse(id);
        return ResponseEntity.ok(paciente);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createPaciente(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse paciente = pacienteService.createPaciente(request);
        return ResponseEntity.ok(paciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaciente(@PathVariable Long id, @Valid @RequestBody PacienteRequest request) {
        PacienteResponse paciente = pacienteService.updatePaciente(id, request);
        return ResponseEntity.ok(paciente);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deletePaciente(@PathVariable Long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.ok("Paciente eliminado correctamente");
    }

    @GetMapping("/dni/{dni}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PacienteResponse> getPacienteByDni(@PathVariable String dni) {
        PacienteResponse paciente = pacienteService.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return ResponseEntity.ok(paciente);
    }
}
