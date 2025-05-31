package com.ingestion.reception.service.impl;

import com.ingestion.patient.model.Patient;
import com.ingestion.patient.repository.PatientRepository;
import com.ingestion.reception.dto.PatientRegistrationDTO;
import com.ingestion.reception.model.PatientRegistration;
import com.ingestion.reception.repository.PatientRegistrationRepository;
import com.ingestion.reception.service.PatientRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientRegistrationServiceImpl implements PatientRegistrationService {

    @Autowired
    private PatientRegistrationRepository patientRegistrationRepository;
    @Autowired
    private PatientRepository patientRepository;



    @Override
    @Transactional
    public PatientRegistrationDTO registerNewPatient(PatientRegistrationDTO registrationDTO) {
        // Create new patient
        Patient patient = new Patient();
        patient.setFirstName(registrationDTO.getFirstName());
        patient.setLastName(registrationDTO.getLastName());
        patient.setEmail(registrationDTO.getEmail());
        patient.setPhoneNumber(registrationDTO.getPhone());
        patient.setGender(Patient.Gender.valueOf(registrationDTO.getGender()));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (registrationDTO.getDateOfBirth() != null && !registrationDTO.getDateOfBirth().isEmpty()) {
            patient.setDateOfBirth(LocalDate.parse(registrationDTO.getDateOfBirth(), formatter));
        }
        
        // Combine address fields into a single address string
        String fullAddress = registrationDTO.getAddress();
        if (registrationDTO.getCity() != null) {
            fullAddress += ", " + registrationDTO.getCity();
        }
        if (registrationDTO.getState() != null) {
            fullAddress += ", " + registrationDTO.getState();
        }
        if (registrationDTO.getZipCode() != null) {
            fullAddress += " " + registrationDTO.getZipCode();
        }
        if (registrationDTO.getCountry() != null) {
            fullAddress += ", " + registrationDTO.getCountry();
        }
        patient.setAddress(fullAddress);
        patient.setEmergencyContactName(registrationDTO.getEmergencyContactName());
        patient.setEmergencyContactPhone(registrationDTO.getEmergencyContactPhone());
        // Store emergency contact relationship in medical history if needed
        if (registrationDTO.getEmergencyContactRelationship() != null) {
            String medicalHistory = patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "";
            medicalHistory += "\nEmergency Contact Relationship: " + registrationDTO.getEmergencyContactRelationship();
            patient.setMedicalHistory(medicalHistory);
        }
        patient.setBloodGroup(registrationDTO.getBloodGroup());
        patient.setAllergies(registrationDTO.getAllergies());
        
        // Store chronic conditions in medical history
        if (registrationDTO.getChronicConditions() != null) {
            String medicalHistory = patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "";
            medicalHistory += "\nChronic Conditions: " + registrationDTO.getChronicConditions();
            patient.setMedicalHistory(medicalHistory);
        }
        
        Patient savedPatient = patientRepository.save(patient);
        
        // Create registration
        PatientRegistration registration = new PatientRegistration();
        registration.setPatient(savedPatient);
        registration.setRegistrationNumber(generateRegistrationNumber(registrationDTO.getRegistrationType()));
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setRegisteredBy(registrationDTO.getRegisteredBy());
        registration.setRegistrationType(registrationDTO.getRegistrationType());
        registration.setRegistrationNotes(registrationDTO.getRegistrationNotes());
        registration.setActive(true);
        
        PatientRegistration savedRegistration = patientRegistrationRepository.save(registration);
        
        return convertToDTO(savedRegistration);
    }

    @Override
    @Transactional
    public PatientRegistrationDTO registerExistingPatient(PatientRegistrationDTO registrationDTO) {
        Patient patient = patientRepository.findById(registrationDTO.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + registrationDTO.getPatientId()));
        
        PatientRegistration registration = new PatientRegistration();
        registration.setPatient(patient);
        registration.setRegistrationNumber(generateRegistrationNumber(registrationDTO.getRegistrationType()));
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setRegisteredBy(registrationDTO.getRegisteredBy());
        registration.setRegistrationType(registrationDTO.getRegistrationType());
        registration.setRegistrationNotes(registrationDTO.getRegistrationNotes());
        registration.setActive(true);
        
        PatientRegistration savedRegistration = patientRegistrationRepository.save(registration);
        
        return convertToDTO(savedRegistration);
    }

    @Override
    public PatientRegistrationDTO getRegistrationById(Long id) {
        PatientRegistration registration = patientRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with ID: " + id));
        
        return convertToDTO(registration);
    }

    @Override
    public PatientRegistrationDTO getRegistrationByNumber(String registrationNumber) {
        PatientRegistration registration = patientRegistrationRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with number: " + registrationNumber));
        
        return convertToDTO(registration);
    }

    @Override
    public List<PatientRegistrationDTO> getRegistrationsByPatientId(Long patientId) {
        List<PatientRegistration> registrations = patientRegistrationRepository.findByPatientId(patientId);
        
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientRegistrationDTO> getRegistrationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<PatientRegistration> registrations = patientRegistrationRepository.findByRegistrationDateBetween(startDate, endDate);
        
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientRegistrationDTO> getRegistrationsByTypeAndDateRange(
            PatientRegistration.RegistrationType type, 
            LocalDateTime startDate, 
            LocalDateTime endDate) {
        List<PatientRegistration> registrations = patientRegistrationRepository.findByRegistrationTypeAndDateBetween(
                type, startDate, endDate);
        
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientRegistrationDTO updateRegistration(Long id, PatientRegistrationDTO registrationDTO) {
        PatientRegistration registration = patientRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with ID: " + id));
        
        registration.setRegistrationNotes(registrationDTO.getRegistrationNotes());
        registration.setActive(registrationDTO.isActive());
        
        PatientRegistration updatedRegistration = patientRegistrationRepository.save(registration);
        
        return convertToDTO(updatedRegistration);
    }

    @Override
    @Transactional
    public void deactivateRegistration(Long id) {
        PatientRegistration registration = patientRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with ID: " + id));
        
        registration.setActive(false);
        patientRegistrationRepository.save(registration);
    }

    @Override
    public String generateRegistrationNumber(PatientRegistration.RegistrationType registrationType) {
        LocalDate today = LocalDate.now();
        String prefix = "REG-" + today.getYear() + String.format("%02d", today.getMonthValue()) + String.format("%02d", today.getDayOfMonth()) + "-";
        
        String lastRegistrationNumber = patientRegistrationRepository.findLastRegistrationNumberWithPrefix(prefix);
        
        int sequence = 1;
        if (lastRegistrationNumber != null) {
            String sequenceStr = lastRegistrationNumber.substring(prefix.length());
            sequence = Integer.parseInt(sequenceStr) + 1;
        }
        
        return prefix + String.format("%04d", sequence);
    }

    @Override
    public long countRegistrationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return patientRegistrationRepository.countRegistrationsByDateRange(startDate, endDate);
    }
    
    private PatientRegistrationDTO convertToDTO(PatientRegistration registration) {
        PatientRegistrationDTO dto = new PatientRegistrationDTO();
        dto.setId(registration.getPatient().getId());
        dto.setPatientId(registration.getPatient().getId());
        dto.setPatientName(registration.getPatient().getFirstName() + " " + registration.getPatient().getLastName());
        dto.setRegistrationNumber(registration.getRegistrationNumber());
        dto.setRegistrationDate(registration.getRegistrationDate());
        dto.setRegisteredBy(registration.getRegisteredBy());
        dto.setRegistrationType(registration.getRegistrationType());
        dto.setRegistrationNotes(registration.getRegistrationNotes());
        dto.setActive(registration.isActive());
        
        return dto;
    }
}