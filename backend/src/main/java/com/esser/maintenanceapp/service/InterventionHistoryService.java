package com.esser.maintenanceapp.service;

import com.esser.maintenanceapp.entity.InterventionHistory;

import java.util.List;

public interface InterventionHistoryService {
    List<InterventionHistory> getHistoryByInterventionId(Long interventionId);
}