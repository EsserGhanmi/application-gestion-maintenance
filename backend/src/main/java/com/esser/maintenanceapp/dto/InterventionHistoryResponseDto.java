package com.esser.maintenanceapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterventionHistoryResponseDto {
    private Long id;
    private String actionType;
    private String oldValue;
    private String newValue;
    private LocalDateTime changedAt;
    private Long changedById;
    private String changedByEmail;
}