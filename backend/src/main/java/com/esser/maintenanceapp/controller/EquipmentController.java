package com.esser.maintenanceapp.controller;

import com.esser.maintenanceapp.dto.EquipmentRequestDto;
import com.esser.maintenanceapp.dto.EquipmentResponseDto;
import com.esser.maintenanceapp.entity.Equipment;
import com.esser.maintenanceapp.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
@Tag(name = "Equipments", description = "Gestion des équipements")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    @Operation(summary = "Créer un équipement")
    public EquipmentResponseDto createEquipment(@Valid @RequestBody EquipmentRequestDto dto) {
        Equipment equipment = Equipment.builder()
                .name(dto.getName())
                .serialNumber(dto.getSerialNumber())
                .model(dto.getModel())
                .status(dto.getStatus())
                .build();

        Equipment savedEquipment = equipmentService.saveEquipment(equipment);

        return EquipmentResponseDto.builder()
                .id(savedEquipment.getId())
                .name(savedEquipment.getName())
                .serialNumber(savedEquipment.getSerialNumber())
                .model(savedEquipment.getModel())
                .status(savedEquipment.getStatus())
                .build();
    }

    @GetMapping
    @Operation(summary = "Lister les équipements")
    public List<EquipmentResponseDto> getAllEquipments() {
        return equipmentService.getAllEquipments().stream()
                .map(equipment -> EquipmentResponseDto.builder()
                        .id(equipment.getId())
                        .name(equipment.getName())
                        .serialNumber(equipment.getSerialNumber())
                        .model(equipment.getModel())
                        .status(equipment.getStatus())
                        .build())
                .toList();
    }
}