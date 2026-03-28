package com.esser.maintenanceapp.dto;

import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterventionRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Status is required")
    private InterventionStatus status;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "CreatedAt is required")
    private LocalDateTime createdAt;

    private LocalDateTime scheduledAt;

    private LocalDateTime closedAt;

    @NotNull(message = "Equipment id is required")
    private Long equipmentId;

    @NotNull(message = "CreatedBy id is required")
    private Long createdById;

    private Long assignedTechnicianId;
}