package com.esser.maintenanceapp.service.impl;

import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.entity.Equipment;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.entity.InterventionHistory;
import com.esser.maintenanceapp.entity.User;
import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.repository.EquipmentRepository;
import com.esser.maintenanceapp.repository.InterventionHistoryRepository;
import com.esser.maintenanceapp.repository.InterventionRepository;
import com.esser.maintenanceapp.repository.UserRepository;
import com.esser.maintenanceapp.service.InterventionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InterventionServiceImpl implements InterventionService {

    private final InterventionRepository interventionRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final InterventionHistoryRepository interventionHistoryRepository;

    public InterventionServiceImpl(
            InterventionRepository interventionRepository,
            EquipmentRepository equipmentRepository,
            UserRepository userRepository,
            InterventionHistoryRepository interventionHistoryRepository
    ) {
        this.interventionRepository = interventionRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.interventionHistoryRepository = interventionHistoryRepository;
    }

    @Override
    public Intervention createIntervention(InterventionRequestDto dto) {
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        User createdBy = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Creator user not found"));

        User assignedTechnician = null;
        if (dto.getAssignedTechnicianId() != null) {
            assignedTechnician = userRepository.findById(dto.getAssignedTechnicianId())
                    .orElseThrow(() -> new RuntimeException("Assigned technician not found"));
        }

        Intervention intervention = Intervention.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .createdAt(dto.getCreatedAt())
                .scheduledAt(dto.getScheduledAt())
                .closedAt(dto.getClosedAt())
                .equipment(equipment)
                .createdBy(createdBy)
                .assignedTechnician(assignedTechnician)
                .build();

        Intervention savedIntervention = interventionRepository.save(intervention);

        InterventionHistory history = InterventionHistory.builder()
                .actionType("CREATED")
                .oldValue(null)
                .newValue("Intervention created")
                .changedAt(LocalDateTime.now())
                .intervention(savedIntervention)
                .changedBy(createdBy)
                .build();

        interventionHistoryRepository.save(history);

        return savedIntervention;
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

    @Override
    public Intervention updateStatus(Long interventionId, InterventionStatus status, Long changedByUserId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        User changedBy = userRepository.findById(changedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String oldStatus = intervention.getStatus().name();
        intervention.setStatus(status);

        if (status == InterventionStatus.COMPLETED) {
            intervention.setClosedAt(LocalDateTime.now());
        }

        Intervention updatedIntervention = interventionRepository.save(intervention);

        InterventionHistory history = InterventionHistory.builder()
                .actionType("STATUS_CHANGED")
                .oldValue(oldStatus)
                .newValue(status.name())
                .changedAt(LocalDateTime.now())
                .intervention(updatedIntervention)
                .changedBy(changedBy)
                .build();

        interventionHistoryRepository.save(history);

        return updatedIntervention;
    }

    @Override
    public Intervention assignTechnician(Long interventionId, Long technicianId, Long changedByUserId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        User changedBy = userRepository.findById(changedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String oldValue = intervention.getAssignedTechnician() != null
                ? intervention.getAssignedTechnician().getId().toString()
                : null;

        intervention.setAssignedTechnician(technician);
        Intervention updatedIntervention = interventionRepository.save(intervention);

        InterventionHistory history = InterventionHistory.builder()
                .actionType("TECHNICIAN_ASSIGNED")
                .oldValue(oldValue)
                .newValue(technician.getId().toString())
                .changedAt(LocalDateTime.now())
                .intervention(updatedIntervention)
                .changedBy(changedBy)
                .build();

        interventionHistoryRepository.save(history);

        return updatedIntervention;
    }
}