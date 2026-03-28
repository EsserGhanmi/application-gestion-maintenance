package com.esser.maintenanceapp.service.impl;

import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.entity.Equipment;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.entity.InterventionHistory;
import com.esser.maintenanceapp.entity.User;
import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.enums.Priority;
import com.esser.maintenanceapp.enums.Role;
import com.esser.maintenanceapp.exception.BadRequestException;
import com.esser.maintenanceapp.exception.ResourceNotFoundException;
import com.esser.maintenanceapp.repository.EquipmentRepository;
import com.esser.maintenanceapp.repository.InterventionHistoryRepository;
import com.esser.maintenanceapp.repository.InterventionRepository;
import com.esser.maintenanceapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterventionServiceImplTest {

    @Mock
    private InterventionRepository interventionRepository;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InterventionHistoryRepository interventionHistoryRepository;

    @InjectMocks
    private InterventionServiceImpl interventionService;

    private Equipment equipment;
    private User manager;
    private User technician;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder().id(10L).name("Pump").serialNumber("SN-1").model("M1").build();
        manager = User.builder().id(1L).email("manager@test.com").role(Role.MANAGER).build();
        technician = User.builder().id(2L).email("tech@test.com").role(Role.TECHNICIAN).build();
    }

    @Test
    void createIntervention_shouldCreateAndSaveHistory() {
        InterventionRequestDto dto = new InterventionRequestDto();
        dto.setTitle("Fix leak");
        dto.setDescription("Pipe leak");
        dto.setPriority(Priority.HIGH);
        dto.setEquipmentId(10L);
        dto.setCreatedById(1L);
        dto.setAssignedTechnicianId(2L);
        dto.setStatus(InterventionStatus.TO_DO);
        dto.setCreatedAt(java.time.LocalDateTime.now());

        when(equipmentRepository.findById(10L)).thenReturn(Optional.of(equipment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(userRepository.findById(2L)).thenReturn(Optional.of(technician));
        when(interventionRepository.save(any(Intervention.class))).thenAnswer(invocation -> {
            Intervention i = invocation.getArgument(0);
            i.setId(100L);
            return i;
        });
        when(interventionHistoryRepository.save(any(InterventionHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Intervention saved = interventionService.createIntervention(dto);

        assertNotNull(saved.getId());
        assertEquals(InterventionStatus.TO_DO, saved.getStatus());
        assertEquals(2L, saved.getAssignedTechnician().getId());
    }

    @Test
    void updateStatus_shouldThrowWhenCompletedWithoutTechnician() {
        Intervention intervention = Intervention.builder().id(100L).status(InterventionStatus.IN_PROGRESS).build();

        when(interventionRepository.findById(100L)).thenReturn(Optional.of(intervention));
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> interventionService.updateStatus(100L, InterventionStatus.COMPLETED, 1L));

        assertTrue(ex.getMessage().contains("without an assigned technician"));
    }

    @Test
    void assignTechnician_shouldAssignWhenRoleIsTechnician() {
        Intervention intervention = Intervention.builder().id(100L).status(InterventionStatus.TO_DO).build();

        when(interventionRepository.findById(100L)).thenReturn(Optional.of(intervention));
        when(userRepository.findById(2L)).thenReturn(Optional.of(technician));
        when(userRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(interventionRepository.save(any(Intervention.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(interventionHistoryRepository.save(any(InterventionHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Intervention updated = interventionService.assignTechnician(100L, 2L, 1L);

        assertNotNull(updated.getAssignedTechnician());
        assertEquals(2L, updated.getAssignedTechnician().getId());
    }

    @Test
    void assignTechnician_shouldThrowWhenInterventionNotFound() {
        when(interventionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> interventionService.assignTechnician(999L, 2L, 1L));
    }
}