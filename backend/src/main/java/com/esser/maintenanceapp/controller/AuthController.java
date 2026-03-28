package com.esser.maintenanceapp.controller;

import com.esser.maintenanceapp.dto.AuthLoginRequestDto;
import com.esser.maintenanceapp.dto.AuthLoginResponseDto;
import com.esser.maintenanceapp.entity.User;
import com.esser.maintenanceapp.exception.BadRequestException;
import com.esser.maintenanceapp.exception.ResourceNotFoundException;
import com.esser.maintenanceapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public AuthLoginResponseDto login(@Valid @RequestBody AuthLoginRequestDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid email or password");
        }

        User user = userService.getUserByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + dto.getEmail()));

        return AuthLoginResponseDto.builder()
                .message("Authentication successful")
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}