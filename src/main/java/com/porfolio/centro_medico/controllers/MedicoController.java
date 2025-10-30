package com.porfolio.centro_medico.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porfolio.centro_medico.models.dto.MedicoRequest;
import com.porfolio.centro_medico.models.dto.MedicoResponse;
import com.porfolio.centro_medico.models.enums.Especialidad;
import com.porfolio.centro_medico.services.MedicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponse>> getAllMedicos() {
        return ResponseEntity.ok(medicoService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> getMedicoById(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.getMedicoResponse(id));
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<MedicoResponse>> getMedicosByEspecialidad(
            @PathVariable Especialidad especialidad) {
        return ResponseEntity.ok(medicoService.findByEspecialidad(especialidad));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createMedico(@Valid @RequestBody MedicoRequest request) {
        System.out.println("=== DEBUG MEDICO CREATE ===");
        System.out.println("Request recibido: " + request);
        System.out.println("Nombre: " + request.nombre());
        System.out.println("Email: " + request.email());

        return ResponseEntity.ok(medicoService.createMedico(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateMedico(@PathVariable Long id, @Valid @RequestBody MedicoRequest request) {
        return ResponseEntity.ok(medicoService.updateMedico(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deactivateMedico(@PathVariable Long id) {
        medicoService.deactivateMedico(id);
        return ResponseEntity.ok("Médico desactivado correctamente");
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> activateMedico(@PathVariable Long id) {
        medicoService.activateMedico(id);
        return ResponseEntity.ok("Médico activado correctamente");
    }
}
