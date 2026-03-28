package com.esser.maintenanceapp.service;

import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.entity.InterventionHistory;

import java.util.List;


public interface InterventionService {
    Intervention createIntervention(InterventionRequestDto dto);

    List<Intervention> getAllInterventions();

    Intervention getInterventionById(Long id);

    void deleteIntervention(Long id);

    Intervention updateStatus(Long interventionId, InterventionStatus status, Long changedByUserId);

    Intervention assignTechnician(Long interventionId, Long technicianId, Long changedByUserId);

    List<InterventionHistory> getInterventionHistory(Long interventionId);
}