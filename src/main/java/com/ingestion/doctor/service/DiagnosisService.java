package com.ingestion.doctor.service;


import com.ingestion.doctor.dto.DiagnosisDTO;
import com.ingestion.doctor.model.Diagnosis;
import com.ingestion.common.model.inpatient.InpatientAdmission;
import com.ingestion.doctor.repository.DiagnosisRepository;
import com.ingestion.common.repository.InpatientAdmissionRepository;

import com.ingestion.doctor.service.DoctorService;
import com.ingestion.patient.service.PatientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiagnosisService {

    private static final Logger log = LoggerFactory.getLogger(DiagnosisService.class);

    @Autowired
    private  DiagnosisRepository diagnosisRepository;
    @Autowired
    private  PatientService patientService;
    @Autowired
    private  DoctorService doctorService;
    @Autowired
    private  InpatientAdmissionRepository inpatientAdmissionRepository;

    @Transactional(readOnly = true)
    public List<Diagnosis> getAllDiagnoses() {
        log.debug("Fetching all diagnoses");
        return diagnosisRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Diagnosis> getDiagnosisById(Long id) {
        log.debug("Fetching diagnosis with ID: {}", id);
        return diagnosisRepository.findById(id);
    }

    @Transactional
    public Diagnosis saveDiagnosis(Diagnosis diagnosis) {
        log.debug("Saving diagnosis: {}", diagnosis);
        return diagnosisRepository.save(diagnosis);
    }

    @Transactional
    public void deleteDiagnosis(Long id) {
        log.debug("Deleting diagnosis with ID: {}", id);
        diagnosisRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> getDiagnosesByPatientId(Long patientId) {
        log.debug("Fetching diagnoses for patient ID: {}", patientId);
        return diagnosisRepository.findByPatientIdOrderByDiagnosisDateDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> getDiagnosesByDoctorId(Long doctorId) {
        log.debug("Fetching diagnoses by doctor ID: {}", doctorId);
        return diagnosisRepository.findByDoctorIdOrderByDiagnosisDateDesc(doctorId);
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> getChronicDiagnosesByPatientId(Long patientId) {
        log.debug("Fetching chronic diagnoses for patient ID: {}", patientId);
        return diagnosisRepository.findByPatientIdAndIsChronicTrue(patientId);
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> getPrimaryDiagnosesByPatientId(Long patientId) {
        log.debug("Fetching primary diagnoses for patient ID: {}", patientId);
        return diagnosisRepository.findByPatientIdAndIsPrimaryTrue(patientId);
    }

    @Transactional
    public Diagnosis createDiagnosis(Long patientId, Long doctorId, DiagnosisDTO diagnosisDTO) {
        log.debug("Creating diagnosis for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        return patientService.getPatientById(patientId)
            .flatMap(patient -> doctorService.getDoctorById(doctorId)
                .map(doctor -> {
                    Diagnosis diagnosis = new Diagnosis();
                    diagnosis.setPatient(patient);
                    diagnosis.setDoctor(doctor);
                    diagnosis.setDiagnosisCode(diagnosisDTO.getDiagnosisCode());
                    diagnosis.setDiagnosisName(diagnosisDTO.getDiagnosisName());
                    diagnosis.setDiagnosisDescription(diagnosisDTO.getDiagnosisDescription());
                    diagnosis.setDiagnosisDate(Optional.ofNullable(diagnosisDTO.getDiagnosisDate())
                        .orElse(LocalDate.now()));
                    diagnosis.setDiagnosisType(diagnosisDTO.getDiagnosisType());
                    diagnosis.setSeverity(diagnosisDTO.getSeverity());
                    diagnosis.setIsPrimary(Optional.ofNullable(diagnosisDTO.getIsPrimary())
                        .orElse(false));
                    diagnosis.setIsChronic(Optional.ofNullable(diagnosisDTO.getIsChronic())
                        .orElse(false));
                    diagnosis.setNotes(diagnosisDTO.getNotes());
                    
                    return diagnosisRepository.save(diagnosis);
                }))
            .orElseThrow(() -> {
                log.error("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
                return new IllegalArgumentException("Patient or Doctor not found");
            });
    }

    @Transactional(readOnly = true)
    public List<InpatientAdmission> getInpatientCareByPatientId(Long patientId) {
        log.debug("Fetching inpatient care for patient ID: {}", patientId);
        return patientService.getPatientById(patientId)
            .map(inpatientAdmissionRepository::findByPatientOrderByAdmissionDateDesc)
            .orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<DiagnosisDTO> mapToDTOs(List<Diagnosis> diagnoses) {
        return diagnoses.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public DiagnosisDTO mapToDTO(Diagnosis diagnosis) {
        DiagnosisDTO dto = new DiagnosisDTO();
        dto.setId(diagnosis.getId());
        dto.setPatientId(diagnosis.getPatient().getId());
        dto.setDoctorId(diagnosis.getDoctor().getId());
        dto.setPatientName(diagnosis.getPatient().getFirstName() + " " + diagnosis.getPatient().getLastName());
        dto.setDoctorName(diagnosis.getDoctor().getFirstName() + " " + diagnosis.getDoctor().getLastName());
        dto.setDiagnosisCode(diagnosis.getDiagnosisCode());
        dto.setDiagnosisName(diagnosis.getDiagnosisName());
        dto.setDiagnosisDescription(diagnosis.getDiagnosisDescription());
        dto.setDiagnosisDate(diagnosis.getDiagnosisDate());
        dto.setDiagnosisType(diagnosis.getDiagnosisType());
        dto.setSeverity(diagnosis.getSeverity());
        dto.setIsPrimary(diagnosis.getIsPrimary());
        dto.setIsChronic(diagnosis.getIsChronic());
        dto.setNotes(diagnosis.getNotes());
        return dto;
    }
}