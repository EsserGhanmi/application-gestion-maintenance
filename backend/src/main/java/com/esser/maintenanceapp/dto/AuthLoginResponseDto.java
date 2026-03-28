package com.esser.maintenanceapp.dto;

import com.esser.maintenanceapp.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthLoginResponseDto {
    private String message;
    private Long userId;
    private String email;
    private Role role;
}