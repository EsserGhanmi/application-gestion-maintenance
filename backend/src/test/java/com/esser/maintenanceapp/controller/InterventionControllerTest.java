package com.esser.maintenanceapp.controller;

import com.esser.maintenanceapp.dto.AssignTechnicianRequestDto;
import com.esser.maintenanceapp.dto.InterventionRequestDto;
import com.esser.maintenanceapp.dto.UpdateStatusRequestDto;
import com.esser.maintenanceapp.entity.Equipment;
import com.esser.maintenanceapp.entity.Intervention;
import com.esser.maintenanceapp.entity.User;
import com.esser.maintenanceapp.enums.InterventionStatus;
import com.esser.maintenanceapp.enums.Priority;
import com.esser.maintenanceapp.enums.Role;
import com.esser.maintenanceapp.exception.GlobalExceptionHandler;
import com.esser.maintenanceapp.exception.ResourceNotFoundException;
import com.esser.maintenanceapp.security.CustomUserDetailsService;
import com.esser.maintenanceapp.security.SecurityConfig;
import com.esser.maintenanceapp.service.InterventionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InterventionController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class InterventionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InterventionService interventionService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void createIntervention_shouldReturnCreatedForManager() throws Exception {
        InterventionRequestDto request = new InterventionRequestDto();
        request.setTitle("New intervention");
        request.setDescription("desc");
        request.setPriority(Priority.MEDIUM);
        request.setEquipmentId(10L);
        request.setCreatedById(1L);
        request.setStatus(InterventionStatus.TO_DO);
        request.setCreatedAt(LocalDateTime.now());

        Intervention intervention = buildIntervention(100L, InterventionStatus.TO_DO);
        when(interventionService.createIntervention(any(InterventionRequestDto.class))).thenReturn(intervention);

        mockMvc.perform(post("/api/interventions")
                        .with(user("manager@test.com").roles("MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.status").value("TO_DO"));
    }

    @Test
    void createIntervention_shouldReturnForbiddenForTechnician() throws Exception {
        InterventionRequestDto request = new InterventionRequestDto();
        request.setTitle("New intervention");
        request.setDescription("desc");
        request.setPriority(Priority.MEDIUM);
        request.setEquipmentId(10L);
        request.setCreatedById(1L);
        request.setStatus(InterventionStatus.TO_DO);
        request.setCreatedAt(LocalDateTime.now());

        mockMvc.perform(post("/api/interventions")
                        .with(user("tech@test.com").roles("TECHNICIAN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateStatus_shouldReturnUpdatedIntervention() throws Exception {
        UpdateStatusRequestDto request = new UpdateStatusRequestDto();
        request.setStatus(InterventionStatus.IN_PROGRESS);
        request.setChangedByUserId(1L);

        Intervention intervention = buildIntervention(100L, InterventionStatus.IN_PROGRESS);
        when(interventionService.updateStatus(eq(100L), eq(InterventionStatus.IN_PROGRESS), eq(1L))).thenReturn(intervention);

        mockMvc.perform(patch("/api/interventions/100/status")
                        .with(user("manager@test.com").roles("MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void assignTechnician_shouldReturn404WhenInterventionNotFound() throws Exception {
        AssignTechnicianRequestDto request = new AssignTechnicianRequestDto();
        request.setTechnicianId(2L);
        request.setChangedByUserId(1L);

        when(interventionService.assignTechnician(eq(999L), eq(2L), eq(1L)))
                .thenThrow(new ResourceNotFoundException("Intervention not found with id: 999"));

        mockMvc.perform(patch("/api/interventions/999/assign")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Intervention not found with id: 999"));
    }

    private Intervention buildIntervention(Long id, InterventionStatus status) {
        return Intervention.builder()
                .id(id)
                .title("Intervention")
                .description("desc")
                .status(status)
                .priority(Priority.HIGH)
                .createdAt(LocalDateTime.now())
                .equipment(Equipment.builder().id(10L).build())
                .createdBy(User.builder().id(1L).role(Role.MANAGER).build())
                .assignedTechnician(User.builder().id(2L).role(Role.TECHNICIAN).build())
                .build();
    }
}