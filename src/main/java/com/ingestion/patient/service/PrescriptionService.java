package com.ingestion.patient.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.pharmacy.model.PrescriptionItem;
import com.ingestion.doctor.repository.DoctorRepository;
import com.ingestion.patient.repository.PatientRepository;
import com.ingestion.pharmacy.repository.PrescriptionRepository;
import com.ingestion.common.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionService.class);
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private NotificationService notificationService;
    

    @Transactional(readOnly = true)
    public List<Prescription> getAllPrescriptions() {
        log.debug("Fetching all prescriptions");
        return prescriptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Prescription> getPrescriptionById(Long id) {
        log.debug("Fetching prescription with ID: {}", id);
        return prescriptionRepository.findById(id);
    }

    @Transactional
    public Prescription savePrescription(Prescription prescription) {
        log.debug("Saving prescription: {}", prescription);
        
        boolean isNew = prescription.getId() == null;
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        
        if (isNew) {
            notificationService.sendPrescriptionNotification(savedPrescription);
        }
        
        return savedPrescription;
    }

    @Transactional
    public void deletePrescription(Long id) {
        log.debug("Deleting prescription with ID: {}", id);
        prescriptionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        log.debug("Fetching prescriptions for patient ID: {}", patientId);
        return prescriptionRepository.findByPatientIdOrderByDateDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getActivePrescriptionsByPatientId(Long patientId) {
        log.debug("Fetching active prescriptions for patient ID: {}", patientId);
        return prescriptionRepository.findActiveByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        log.debug("Fetching prescriptions for doctor ID: {}", doctorId);
        return prescriptionRepository.findByDoctorIdOrderByDateDesc(doctorId);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByPatientIdAndDateRange(Long patientId, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching prescriptions for patient ID: {} between {} and {}", patientId, startDate, endDate);
        return prescriptionRepository.findByPatientIdAndDateRange(patientId, startDate, endDate);
    }

    @Transactional
    public Prescription createPrescription(Long patientId, Long doctorId, String diagnosis, LocalDate followUpDate, List<PrescriptionItem> items) {
        log.debug("Creating prescription for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        
        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();
            
            Prescription prescription = new Prescription();
            prescription.setPatient(patient);
            prescription.setDoctor(doctor);
            prescription.setPrescriptionDate(LocalDateTime.now());
            prescription.setDiagnosis(diagnosis);
            prescription.setNotes("Follow-up date: " + followUpDate);
            prescription.setStatus(Prescription.PrescriptionStatus.ACTIVE);
            
            Prescription savedPrescription = prescriptionRepository.save(prescription);
            
            // Add prescription items
            for (PrescriptionItem item : items) {
                item.setPrescription(savedPrescription);
                savedPrescription.addPrescriptionItem(item);
            }
            
            savedPrescription = prescriptionRepository.save(savedPrescription);
            
            notificationService.sendPrescriptionNotification(savedPrescription);
            
            return savedPrescription;
        } else {
            log.error("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
            throw new IllegalArgumentException("Patient or Doctor not found");
        }
    }

    @Transactional
    public void deactivatePrescription(Long prescriptionId) {
        log.debug("Deactivating prescription with ID: {}", prescriptionId);
        
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(prescriptionId);
        
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            prescription.setStatus(Prescription.PrescriptionStatus.CANCELLED);
            prescriptionRepository.save(prescription);
        } else {
            log.error("Prescription ID: {} not found", prescriptionId);
            throw new IllegalArgumentException("Prescription not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Prescription> searchPrescriptionsByMedication(String medicationName) {
        log.debug("Searching prescriptions with medication: {}", medicationName);
        return prescriptionRepository.findByMedicationName(medicationName);
    }
    
    @Transactional
    public Prescription createPrescriptionFromDTO(com.ingestion.doctor.dto.PrescriptionDTO prescriptionDTO) {
        log.debug("Creating prescription from DTO for patient ID: {} by doctor ID: {}", 
                prescriptionDTO.getPatientId(), prescriptionDTO.getDoctorId());
        
        // This method needs to be updated to work with the pharmacy model
        // We need to fetch Medicine entities for each prescription item
        // For now, we'll create a simplified version
        
        List<PrescriptionItem> items = new ArrayList<>();
        // The actual implementation would need to fetch Medicine entities
        // and create proper PrescriptionItem objects
        
        // Create the prescription
        return createPrescription(
            prescriptionDTO.getPatientId(),
            prescriptionDTO.getDoctorId(),
            prescriptionDTO.getDiagnosis(),
            prescriptionDTO.getFollowUpDate(),
            items
        );
    }
}