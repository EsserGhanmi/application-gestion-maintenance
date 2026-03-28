package com.esser.maintenanceapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTechnicianRequestDto {

    @NotNull(message = "technicianId is required")
    private Long technicianId;

    @NotNull(message = "changedByUserId is required")
    private Long changedByUserId;
}