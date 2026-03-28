package com.esser.maintenanceapp.service;

import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.enums.InterventionStatus;

import java.util.List;
import java.util.Optional;

public interface InterventionService {
    Intervention createIntervention(InterventionRequestDto dto);

    List<Intervention> getAllInterventions();

    Optional<Intervention> getInterventionById(Long id);

    void deleteIntervention(Long id);

    Intervention updateStatus(Long interventionId, InterventionStatus status, Long changedByUserId);

    Intervention assignTechnician(Long interventionId, Long technicianId, Long changedByUserId);
}