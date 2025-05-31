package com.ingestion.reception.service;

import com.ingestion.reception.dto.PatientRegistrationDTO;
import com.ingestion.reception.model.PatientRegistration;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientRegistrationService {
    
    /**
     * Register a new patient
     * @param registrationDTO the registration data
     * @return the created registration
     */
    PatientRegistrationDTO registerNewPatient(PatientRegistrationDTO registrationDTO);
    
    /**
     * Register an existing patient
     * @param registrationDTO the registration data
     * @return the created registration
     */
    PatientRegistrationDTO registerExistingPatient(PatientRegistrationDTO registrationDTO);
    
    /**
     * Get a registration by ID
     * @param id the registration ID
     * @return the registration
     */
    PatientRegistrationDTO getRegistrationById(Long id);
    
    /**
     * Get a registration by registration number
     * @param registrationNumber the registration number
     * @return the registration
     */
    PatientRegistrationDTO getRegistrationByNumber(String registrationNumber);
    
    /**
     * Get all registrations for a patient
     * @param patientId the patient ID
     * @return list of registrations
     */
    List<PatientRegistrationDTO> getRegistrationsByPatientId(Long patientId);
    
    /**
     * Get registrations by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of registrations
     */
    List<PatientRegistrationDTO> getRegistrationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get registrations by type and date range
     * @param type the registration type
     * @param startDate the start date
     * @param endDate the end date
     * @return list of registrations
     */
    List<PatientRegistrationDTO> getRegistrationsByTypeAndDateRange(
            PatientRegistration.RegistrationType type, 
            LocalDateTime startDate, 
            LocalDateTime endDate);
    
    /**
     * Update a registration
     * @param id the registration ID
     * @param registrationDTO the updated registration data
     * @return the updated registration
     */
    PatientRegistrationDTO updateRegistration(Long id, PatientRegistrationDTO registrationDTO);
    
    /**
     * Deactivate a registration
     * @param id the registration ID
     */
    void deactivateRegistration(Long id);
    
    /**
     * Generate a new registration number
     * @param registrationType the registration type
     * @return the generated registration number
     */
    String generateRegistrationNumber(PatientRegistration.RegistrationType registrationType);
    
    /**
     * Count registrations by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return the count of registrations
     */
    long countRegistrationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}