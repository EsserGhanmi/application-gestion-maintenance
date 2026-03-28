package com.esser.maintenanceapp.dto;

import com.esser.maintenanceapp.enums.InterventionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequestDto {

    @NotNull(message = "Status is required")
    private InterventionStatus status;

    @NotNull(message = "changedByUserId is required")
    private Long changedByUserId;
}