package com.esser.maintenanceapp.service.impl;

import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.entity.Equipment;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.enums.Role;
import com.esser.maintenanceapp.exception.BadRequestException;
import com.esser.maintenanceapp.exception.ResourceNotFoundException;
import com.esser.maintenanceapp.entity.InterventionHistory;
import com.esser.maintenanceapp.entity.User;
import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.repository.EquipmentRepository;
import com.esser.maintenanceapp.repository.InterventionHistoryRepository;
import com.esser.maintenanceapp.repository.InterventionRepository;
import com.esser.maintenanceapp.repository.UserRepository;
import com.esser.maintenanceapp.service.InterventionService;
import org.springframework.stereotype.Service;
import com.esser.maintenanceapp.enums.Priority;


import java.time.LocalDateTime;

import java.util.Comparator;
import java.util.List;


@Service
public class InterventionServiceImpl implements InterventionService {

    private static final String ACTION_CREATED = "CREATED";
    private static final String ACTION_STATUS_CHANGED = "STATUS_CHANGED";
    private static final String ACTION_TECHNICIAN_ASSIGNED = "TECHNICIAN_ASSIGNED";

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
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + dto.getEquipmentId()));

        User createdBy = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getCreatedById()));

        User assignedTechnician = null;
        if (dto.getAssignedTechnicianId() != null) {
            assignedTechnician = userRepository.findById(dto.getAssignedTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + dto.getAssignedTechnicianId()));
            validateTechnicianRole(assignedTechnician);
        }
        InterventionStatus status = dto.getStatus() != null ? dto.getStatus() : InterventionStatus.TO_DO;
        if (status == InterventionStatus.COMPLETED && assignedTechnician == null) {
            throw new BadRequestException("Cannot create a COMPLETED intervention without an assigned technician");
        }

        Intervention intervention = Intervention.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(status)
                .priority(dto.getPriority())
                .createdAt(LocalDateTime.now())
                .scheduledAt(dto.getScheduledAt())
                .closedAt(dto.getClosedAt())
                .equipment(equipment)
                .createdBy(createdBy)
                .assignedTechnician(assignedTechnician)
                .build();

        Intervention savedIntervention = interventionRepository.save(intervention);

        saveHistory(savedIntervention, createdBy, ACTION_CREATED, null,
                "Intervention created with status " + savedIntervention.getStatus().name());

        return savedIntervention;
    }

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }
    @Override
    public List<Intervention> getAllInterventions(InterventionStatus status, Priority priority, Long assignedTechnicianId) {
        if (status != null && priority != null && assignedTechnicianId != null) {
            return interventionRepository.findByStatusAndPriorityAndAssignedTechnicianId(status, priority, assignedTechnicianId);
        }
        if (status != null && priority != null) {
            return interventionRepository.findByStatusAndPriority(status, priority);
        }
        if (status != null && assignedTechnicianId != null) {
            return interventionRepository.findByStatusAndAssignedTechnicianId(status, assignedTechnicianId);
        }
        if (priority != null && assignedTechnicianId != null) {
            return interventionRepository.findByPriorityAndAssignedTechnicianId(priority, assignedTechnicianId);
        }
        if (status != null) {
            return interventionRepository.findByStatus(status);
        }
        if (priority != null) {
            return interventionRepository.findByPriority(priority);
        }
        if (assignedTechnicianId != null) {
            return interventionRepository.findByAssignedTechnicianId(assignedTechnicianId);
        }
        return interventionRepository.findAll();
    }


    @Override
    public Intervention getInterventionById(Long id) {
        return interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found with id: " + id));
    }

    @Override
    public void deleteIntervention(Long id) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found with id: " + id));
        interventionRepository.delete(intervention);
    }

    @Override
    public Intervention updateStatus(Long interventionId, InterventionStatus status, Long changedByUserId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found with id: " + interventionId));

        User changedBy = userRepository.findById(changedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + changedByUserId));

        InterventionStatus oldStatus = intervention.getStatus();
        validateStatusTransition(intervention, oldStatus, status);
        intervention.setStatus(status);

        if (status == InterventionStatus.COMPLETED) {
            intervention.setClosedAt(LocalDateTime.now());
        } else {
            intervention.setClosedAt(null);
        }

        Intervention updatedIntervention = interventionRepository.save(intervention);

        saveHistory(updatedIntervention, changedBy, ACTION_STATUS_CHANGED,
                oldStatus != null ? oldStatus.name() : null,
                status.name());

        return updatedIntervention;
    }

    @Override
    public Intervention assignTechnician(Long interventionId, Long technicianId, Long changedByUserId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found with id: " + interventionId));

        User technician = userRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + technicianId));
        validateTechnicianRole(technician);

        User changedBy = userRepository.findById(changedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + changedByUserId));

        String oldValue = intervention.getAssignedTechnician() != null
                ? intervention.getAssignedTechnician().getId().toString()
                : null;

        intervention.setAssignedTechnician(technician);
        Intervention updatedIntervention = interventionRepository.save(intervention);
        saveHistory(updatedIntervention, changedBy, ACTION_TECHNICIAN_ASSIGNED,
                oldValue,
                technician.getId().toString());

        return updatedIntervention;
    }

    @Override
    public List<InterventionHistory> getInterventionHistory(Long interventionId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found with id: " + interventionId));

        return interventionHistoryRepository.findByInterventionId(intervention.getId()).stream()
                .sorted(Comparator.comparing(InterventionHistory::getChangedAt))
                .toList();
    }

    private void validateTechnicianRole(User technician) {
        if (technician.getRole() != Role.TECHNICIAN) {
            throw new BadRequestException("User with id " + technician.getId() + " is not TECHNICIAN");
        }
    }
    private void validateStatusTransition(
            Intervention intervention,
            InterventionStatus oldStatus,
            InterventionStatus newStatus
    ) {
        if (oldStatus == newStatus) {
            throw new BadRequestException("Intervention is already in status " + newStatus.name());
        }

        if ((oldStatus == InterventionStatus.COMPLETED || oldStatus == InterventionStatus.CANCELLED)
                && newStatus != oldStatus) {
            throw new BadRequestException("Cannot change status after intervention is " + oldStatus.name());
        }

        if (newStatus == InterventionStatus.COMPLETED && intervention.getAssignedTechnician() == null) {
            throw new BadRequestException("Cannot mark intervention as COMPLETED without an assigned technician");
        }

        if (oldStatus == InterventionStatus.IN_PROGRESS && newStatus == InterventionStatus.TO_DO) {
            throw new BadRequestException("Cannot move status back from IN_PROGRESS to TO_DO");
        }
    }


    private void saveHistory(
            Intervention intervention,
            User changedBy,
            String actionType,
            String oldValue,
            String newValue
    ) {

        InterventionHistory history = InterventionHistory.builder()
                .actionType(actionType)
                .oldValue(oldValue)
                .newValue(newValue)
                .changedAt(LocalDateTime.now())
                .intervention(intervention)
                .changedBy(changedBy)
                .build();

        interventionHistoryRepository.save(history);


    }
}