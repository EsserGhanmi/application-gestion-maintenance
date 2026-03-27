package com.esser.maintenanceapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Serial number is required")
    private String serialNumber;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Status is required")
    private String status;
}