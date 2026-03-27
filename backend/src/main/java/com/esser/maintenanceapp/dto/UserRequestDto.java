package com.esser.maintenanceapp.dto;

import com.esser.maintenanceapp.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}