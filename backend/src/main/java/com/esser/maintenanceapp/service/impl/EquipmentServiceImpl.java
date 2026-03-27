package com.esser.maintenanceapp.service.impl;

import com.esser.maintenanceapp.entity.Equipment;
import com.esser.maintenanceapp.repository.EquipmentRepository;
import com.esser.maintenanceapp.service.EquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public Equipment saveEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    @Override
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    @Override
    public Optional<Equipment> getEquipmentBySerialNumber(String serialNumber) {
        return equipmentRepository.findBySerialNumber(serialNumber);
    }

    @Override
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }
}