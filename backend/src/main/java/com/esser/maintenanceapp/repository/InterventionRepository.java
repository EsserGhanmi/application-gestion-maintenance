package com.esser.maintenanceapp.repository;

import com.esser.maintenanceapp.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.enums.Priority;
import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    List<Intervention> findByStatus(InterventionStatus status);

    List<Intervention> findByPriority(Priority priority);

    List<Intervention> findByAssignedTechnicianId(Long assignedTechnicianId);

    List<Intervention> findByStatusAndPriority(InterventionStatus status, Priority priority);

    List<Intervention> findByStatusAndAssignedTechnicianId(InterventionStatus status, Long assignedTechnicianId);

    List<Intervention> findByPriorityAndAssignedTechnicianId(Priority priority, Long assignedTechnicianId);

    List<Intervention> findByStatusAndPriorityAndAssignedTechnicianId(
            InterventionStatus status,
            Priority priority,
            Long assignedTechnicianId
    );
}