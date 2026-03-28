package com.esser.maintenanceapp.controller;

import com.esser.maintenanceapp.dto.AssignTechnicianRequestDto;
import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.dto.InterventionResponseDto;
import com.esser.maintenanceapp.dto.UpdateStatusRequestDto;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.entity.InterventionHistory;
import com.esser.maintenanceapp.service.InterventionHistoryService;
import com.esser.maintenanceapp.service.InterventionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interventions")
public class InterventionController {

    private final InterventionService interventionService;
    private final InterventionHistoryService interventionHistoryService;

    public InterventionController(InterventionService interventionService, InterventionHistoryService interventionHistoryService) {
        this.interventionService = interventionService;
        this.interventionHistoryService = interventionHistoryService;
    }

    @PostMapping
    public InterventionResponseDto createIntervention(@Valid @RequestBody InterventionRequestDto dto) {
        Intervention saved = interventionService.createIntervention(dto);
        return mapToResponseDto(saved);
    }

    @GetMapping
    public List<InterventionResponseDto> getAllInterventions() {
        return interventionService.getAllInterventions().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public InterventionResponseDto getInterventionById(@PathVariable Long id) {
        Intervention intervention = interventionService.getInterventionById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));
        return mapToResponseDto(intervention);
    }

    @DeleteMapping("/{id}")
    public void deleteIntervention(@PathVariable Long id) {
        interventionService.deleteIntervention(id);
    }

    @PatchMapping("/{id}/status")
    public InterventionResponseDto updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequestDto dto
    ) {
        Intervention updated = interventionService.updateStatus(id, dto.getStatus(), dto.getChangedByUserId());
        return mapToResponseDto(updated);
    }

    @PatchMapping("/{id}/assign")
    public InterventionResponseDto assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AssignTechnicianRequestDto dto
    ) {
        Intervention updated = interventionService.assignTechnician(id, dto.getTechnicianId(), dto.getChangedByUserId());
        return mapToResponseDto(updated);
    }
    @GetMapping("/{id}/history")
    public List<InterventionHistory> getInterventionHistory(@PathVariable Long id) {
        return interventionHistoryService.getHistoryByInterventionId(id);
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
}