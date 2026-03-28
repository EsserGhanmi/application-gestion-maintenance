package com.esser.maintenanceapp.dto;

import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.enums.Priority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InterventionResponseDto {
    private Long id;
    private String title;
    private String description;
    private InterventionStatus status;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime closedAt;
    private Long equipmentId;
    private Long createdById;
    private Long assignedTechnicianId;
}