package com.ingestion.doctor.service;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.common.laboratory.LabOrderService;
import com.ingestion.doctor.dto.LabOrderDTO;
import com.ingestion.doctor.dto.LabTestDTO;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.laboratory.model.LabTest;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter service that provides doctor-specific lab order functionality
 * by delegating to the common LabOrderService
 */
@Service
public class DoctorLabOrderServiceAdapter {

    @Autowired
    @Qualifier("commonLabOrderService")
    private LabOrderService labOrderService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;
    
    // We don't need the LabTestService for this adapter
    
    /**
     * Get all lab test types for the doctor's order form
     */
    public List<String> getAllLabTestTypes() {
        // This is a simplified implementation - in a real system, you would fetch from a database
        return Arrays.asList(
            "Complete Blood Count (CBC)",
            "Basic Metabolic Panel",
            "Comprehensive Metabolic Panel",
            "Lipid Panel",
            "Liver Function Tests",
            "Thyroid Function Tests",
            "Hemoglobin A1C",
            "Urinalysis",
            "Coagulation Panel",
            "Vitamin D Level"
        );
    }
    
    /**
     * Create a lab order from the DTO submitted by the doctor
     */
    public LabOrder createLabOrderFromDTO(LabOrderDTO labOrderDTO) {
        // Get the patient and doctor
        Optional<Patient> patientOpt = patientService.getPatientById(labOrderDTO.getPatientId());
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(labOrderDTO.getDoctorId());
        
        if (!patientOpt.isPresent() || !doctorOpt.isPresent()) {
            throw new IllegalArgumentException("Patient or Doctor not found");
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        // Convert DTO test list to domain objects
        List<LabTest> tests = new ArrayList<>();
        for (LabTestDTO testDTO : labOrderDTO.getLabTests()) {
            if (testDTO.getTestName() != null && !testDTO.getTestName().isEmpty()) {
                LabTest test = new LabTest();
                test.setTestName(testDTO.getTestName());
                test.setTestCode(testDTO.getTestCode());
                test.setSpecialInstructions(testDTO.getSpecialInstructions());
                // Set required fields for the common LabTest class
                test.setPrice(new java.math.BigDecimal("0.00"));
                test.setIsActive(true);
                tests.add(test);
            }
        }
        
        // Create the order using the common service
        return labOrderService.createOrder(
                patient,
                doctor,
                null, // No OPD visit in this context
                LabOrder.OrderPriority.valueOf(labOrderDTO.getOrderPriority()),
                labOrderDTO.getClinicalNotes(),
                labOrderDTO.getSpecialInstructions(),
                tests
        );
    }
    
    /**
     * Get a lab order by ID
     */
    public Optional<LabOrder> getLabOrderById(Long id) {
        return labOrderService.findById(id);
    }
    
    /**
     * Get all lab orders for a specific doctor
     */
    public List<LabOrder> getLabOrdersByDoctorId(Long doctorId) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (!doctorOpt.isPresent()) {
            return new ArrayList<>();
        }
        
        return labOrderService.findByDoctor(doctorOpt.get());
    }
    
    /**
     * Get pending lab orders for a specific doctor
     */
    public List<LabOrder> getPendingLabOrdersByDoctorId(Long doctorId) {
        List<LabOrder> doctorOrders = getLabOrdersByDoctorId(doctorId);
        
        // Filter to only include pending orders
        List<LabOrder.OrderStatus> pendingStatuses = Arrays.asList(
                LabOrder.OrderStatus.ORDERED,
                LabOrder.OrderStatus.SAMPLE_COLLECTED,
                LabOrder.OrderStatus.IN_PROCESS
        );
        
        return doctorOrders.stream()
                .filter(order -> pendingStatuses.contains(order.getStatus()))
                .collect(Collectors.toList());
    }
}