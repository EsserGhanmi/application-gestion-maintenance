package com.esser.maintenanceapp.controller;

import com.esser.maintenanceapp.dto.UserRequestDto;
import com.esser.maintenanceapp.dto.UserResponseDto;
import com.esser.maintenanceapp.entity.User;
import com.esser.maintenanceapp.service.UserService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur")
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();

        User savedUser = userService.saveUser(user);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @GetMapping
    @Operation(summary = "Lister les utilisateurs")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }
}