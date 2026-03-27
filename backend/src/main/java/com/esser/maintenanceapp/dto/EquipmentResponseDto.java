package com.esser.maintenanceapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EquipmentResponseDto {
    private Long id;
    private String name;
    private String serialNumber;
    private String model;
    private String status;
}