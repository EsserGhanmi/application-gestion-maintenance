package com.esser.maintenanceapp.repository;

import com.esser.maintenanceapp.entity.InterventionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterventionHistoryRepository extends JpaRepository<InterventionHistory, Long> {
    List<InterventionHistory> findByInterventionId(Long interventionId);
}