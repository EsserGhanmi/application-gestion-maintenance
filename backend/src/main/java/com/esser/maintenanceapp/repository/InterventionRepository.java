package com.esser.maintenanceapp.repository;

import com.esser.maintenanceapp.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
}