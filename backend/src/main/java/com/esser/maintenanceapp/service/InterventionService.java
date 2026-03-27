package com.esser.maintenanceapp.service;

import com.esser.maintenanceapp.entity.Intervention;

import java.util.List;
import java.util.Optional;

public interface InterventionService {
    Intervention saveIntervention(Intervention intervention);
    List<Intervention> getAllInterventions();
    Optional<Intervention> getInterventionById(Long id);
    void deleteIntervention(Long id);
}