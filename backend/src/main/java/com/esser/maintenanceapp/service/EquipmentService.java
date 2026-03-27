package com.esser.maintenanceapp.service;

import com.esser.maintenanceapp.entity.Equipment;

import java.util.List;
import java.util.Optional;

public interface EquipmentService {
    Equipment saveEquipment(Equipment equipment);
    List<Equipment> getAllEquipments();
    Optional<Equipment> getEquipmentById(Long id);
    Optional<Equipment> getEquipmentBySerialNumber(String serialNumber);
    void deleteEquipment(Long id);
}