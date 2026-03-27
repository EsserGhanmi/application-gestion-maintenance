package com.esser.maintenanceapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentRequestDto {
    private String name;
    private String serialNumber;
    private String model;
    private String status;
}