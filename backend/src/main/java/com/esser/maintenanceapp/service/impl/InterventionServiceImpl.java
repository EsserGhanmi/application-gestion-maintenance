package com.esser.maintenanceapp.service.impl;

import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.repository.InterventionRepository;
import com.esser.maintenanceapp.service.InterventionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InterventionServiceImpl implements InterventionService {

    private final InterventionRepository interventionRepository;

    public InterventionServiceImpl(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    @Override
    public Intervention saveIntervention(Intervention intervention) {
        return interventionRepository.save(intervention);
    }

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public Optional<Intervention> getInterventionById(Long id) {
        return interventionRepository.findById(id);
    }

    @Override
    public void deleteIntervention(Long id) {
        interventionRepository.deleteById(id);
    }
}