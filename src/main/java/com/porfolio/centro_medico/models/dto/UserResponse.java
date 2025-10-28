package com.porfolio.centro_medico.models.dto;

import com.porfolio.centro_medico.models.enums.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        Boolean isActive) {

}
