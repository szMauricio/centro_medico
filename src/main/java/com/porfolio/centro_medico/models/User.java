package com.porfolio.centro_medico.models;

import java.time.LocalDateTime;

import com.porfolio.centro_medico.models.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 13)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ROLE_ADMIN, ROLE_MEDICO, ROLE_USER

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relaciones OPCIONALES (pueden ser null)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Paciente paciente;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Medico medico;

    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;
    }

    public User(String username, String password, String email, Role role) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        // Si es el mismo paciente, no hacer nada
        if (this.paciente == paciente) {
            return;
        }

        // Desconectar del paciente anterior
        Paciente oldPaciente = this.paciente;
        if (oldPaciente != null) {
            oldPaciente.setUserWithoutCallback(null);
        }

        // Conectar al nuevo paciente
        this.paciente = paciente;
        if (paciente != null) {
            paciente.setUserWithoutCallback(this);
        }
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        // Si es el mismo medico, no hacer nada
        if (this.medico == medico) {
            return;
        }

        // Desconectar del medico anterior
        Medico oldMedico = this.medico;
        if (oldMedico != null) {
            oldMedico.setUserWithoutCallback(null);
        }

        // Conectar al nuevo medico
        this.medico = medico;
        if (medico != null) {
            medico.setUserWithoutCallback(this);
        }
    }

    protected void setPacienteWithoutCallback(Paciente paciente) {
        this.paciente = paciente;
    }

    protected void setMedicoWithoutCallback(Medico medico) {
        this.medico = medico;
    }

}
