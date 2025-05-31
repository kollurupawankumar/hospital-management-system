package com.ingestion.patient.service;

import com.ingestion.patient.model.Patient;
import com.ingestion.patient.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientService.class);
    @Autowired
    private PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        log.debug("Fetching all patients");
        return patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatientById(Long id) {
        log.debug("Fetching patient with ID: {}", id);
        return patientRepository.findById(id);
    }

    @Transactional
    public Patient savePatient(Patient patient) {
        log.debug("Saving patient: {}", patient);
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        log.debug("Deleting patient with ID: {}", id);
        patientRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Patient> searchPatients(String searchTerm) {
        log.debug("Searching patients with term: {}", searchTerm);
        return patientRepository.searchPatients(searchTerm);
    }

    @Transactional(readOnly = true)
    public Patient findByPhoneNumber(String phoneNumber) {
        log.debug("Finding patient by phone number: {}", phoneNumber);
        return patientRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = true)
    public Patient findByEmail(String email) {
        log.debug("Finding patient by email: {}", email);
        return patientRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Patient> findByLastName(String lastName) {
        log.debug("Finding patients by last name: {}", lastName);
        return patientRepository.findByLastNameContainingIgnoreCase(lastName);
    }
    
    @Transactional(readOnly = true)
    public List<Patient> getPatientsByDoctorId(Long doctorId) {
        log.debug("Fetching patients for doctor ID: {}", doctorId);
        // This would typically query a relationship between patients and doctors
        // For now, we'll return an empty list as a placeholder
        return List.of();
    }
    
    @Transactional(readOnly = true)
    public List<Patient> searchPatientsByDoctorId(Long doctorId, String searchTerm) {
        log.debug("Searching patients for doctor ID: {} with term: {}", doctorId, searchTerm);
        // This would typically search patients related to a specific doctor
        // For now, we'll return an empty list as a placeholder
        return List.of();
    }
    
    @Transactional(readOnly = true)
    public List<Patient> getRecentPatientsByDoctorId(Long doctorId, int limit) {
        log.debug("Fetching recent patients for doctor ID: {} with limit: {}", doctorId, limit);
        // This would typically get the most recently seen patients for a doctor
        // For now, we'll return an empty list as a placeholder
        return List.of();
    }
    
    @Transactional(readOnly = true)
    public Optional<Patient> findById(Long id) {
        log.debug("Finding patient by ID: {}", id);
        return patientRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        log.debug("Finding all patients");
        return patientRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Patient> findRecentlyAdmittedPatients(int limit) {
        log.debug("Finding recently admitted patients with limit: {}", limit);
        // This would typically get the most recently admitted patients
        // For now, we'll return an empty list as a placeholder
        return List.of();
    }
}