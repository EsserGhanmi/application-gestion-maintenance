package com.esser.maintenanceapp.service.impl;

import com.esser.maintenanceapp.entity.InterventionHistory;
import com.esser.maintenanceapp.repository.InterventionHistoryRepository;
import com.esser.maintenanceapp.service.InterventionHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterventionHistoryServiceImpl implements InterventionHistoryService {

    private final InterventionHistoryRepository interventionHistoryRepository;

    public InterventionHistoryServiceImpl(InterventionHistoryRepository interventionHistoryRepository) {
        this.interventionHistoryRepository = interventionHistoryRepository;
    }

    @Override
    public List<InterventionHistory> getHistoryByInterventionId(Long interventionId) {
        return interventionHistoryRepository.findByInterventionId(interventionId);
    }
}