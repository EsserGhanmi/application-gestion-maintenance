package com.esser.maintenanceapp.controller;

import com.esser.maintenanceapp.dto.AssignTechnicianRequestDto;
import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.dto.InterventionResponseDto;
import com.esser.maintenanceapp.dto.UpdateStatusRequestDto;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.entity.InterventionHistory;
import com.esser.maintenanceapp.service.InterventionService;
import com.esser.maintenanceapp.dto.InterventionHistoryResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.enums.Priority;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/interventions")
@Tag(name = "Interventions", description = "Gestion des interventions")
public class InterventionController {

    private final InterventionService interventionService;


    public InterventionController(InterventionService interventionService) {
        this.interventionService = interventionService;

    }

    @PostMapping
    @Operation(summary = "Créer une intervention")
    public InterventionResponseDto createIntervention(@Valid @RequestBody InterventionRequestDto dto) {
        Intervention saved = interventionService.createIntervention(dto);
        return mapToResponseDto(saved);
    }

    @GetMapping
    @Operation(summary = "Lister les interventions", description = "Filtres disponibles: status, priority, assignedTechnicianId")
    public List<InterventionResponseDto> getAllInterventions(
            @RequestParam(required = false) InterventionStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long assignedTechnicianId
    ) {
        return interventionService.getAllInterventions(status, priority, assignedTechnicianId).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une intervention par id")
    public InterventionResponseDto getInterventionById(@PathVariable Long id) {
        Intervention intervention = interventionService.getInterventionById(id);
        return mapToResponseDto(intervention);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une intervention")
    public void deleteIntervention(@PathVariable Long id) {
        interventionService.deleteIntervention(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'une intervention")
    public InterventionResponseDto updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequestDto dto
    ) {
        Intervention updated = interventionService.updateStatus(id, dto.getStatus(), dto.getChangedByUserId());
        return mapToResponseDto(updated);
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Affecter un technicien")
    public InterventionResponseDto assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AssignTechnicianRequestDto dto
    ) {
        Intervention updated = interventionService.assignTechnician(id, dto.getTechnicianId(), dto.getChangedByUserId());
        return mapToResponseDto(updated);
    }
    @GetMapping("/{id}/history")
    @Operation(summary = "Consulter l'historique d'une intervention")
    public List<InterventionHistoryResponseDto> getInterventionHistory(@PathVariable Long id) {
        return interventionService.getInterventionHistory(id).stream()
                .map(this::mapHistoryToResponseDto)
                .toList();
    }

    private InterventionResponseDto mapToResponseDto(Intervention intervention) {
        return InterventionResponseDto.builder()
                .id(intervention.getId())
                .title(intervention.getTitle())
                .description(intervention.getDescription())
                .status(intervention.getStatus())
                .priority(intervention.getPriority())
                .createdAt(intervention.getCreatedAt())
                .scheduledAt(intervention.getScheduledAt())
                .closedAt(intervention.getClosedAt())
                .equipmentId(intervention.getEquipment() != null ? intervention.getEquipment().getId() : null)
                .createdById(intervention.getCreatedBy() != null ? intervention.getCreatedBy().getId() : null)
                .assignedTechnicianId(intervention.getAssignedTechnician() != null ? intervention.getAssignedTechnician().getId() : null)
                .build();
    }
    private InterventionHistoryResponseDto mapHistoryToResponseDto(InterventionHistory history) {
        return InterventionHistoryResponseDto.builder()
                .id(history.getId())
                .actionType(history.getActionType())
                .oldValue(history.getOldValue())
                .newValue(history.getNewValue())
                .changedAt(history.getChangedAt())
                .changedById(history.getChangedBy() != null ? history.getChangedBy().getId() : null)
                .changedByEmail(history.getChangedBy() != null ? history.getChangedBy().getEmail() : null)
                .build();
    }
}