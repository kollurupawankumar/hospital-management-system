package com.ingestion.patient.service;

import com.ingestion.patient.model.MedicalRecord;
import com.ingestion.patient.repository.MedicalRecordRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    private static final Logger log = LoggerFactory.getLogger(MedicalRecordService.class);

    @Transactional(readOnly = true)
    public List<MedicalRecord> getAllMedicalRecords() {
        log.debug("Fetching all medical records");
        return medicalRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        log.debug("Fetching medical record with ID: {}", id);
        return medicalRecordRepository.findById(id);
    }

    @Transactional
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        log.debug("Saving medical record: {}", medicalRecord);
        return medicalRecordRepository.save(medicalRecord);
    }

    @Transactional
    public void deleteMedicalRecord(Long id) {
        log.debug("Deleting medical record with ID: {}", id);
        medicalRecordRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        log.debug("Fetching medical records for patient ID: {}", patientId);
        return medicalRecordRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getMedicalRecordsByPatientIdOrderByDateDesc(Long patientId) {
        log.debug("Fetching medical records for patient ID: {} ordered by date descending", patientId);
        return medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getMedicalRecordsByPatientIdAndDateRange(
            Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching medical records for patient ID: {} between {} and {}", 
                patientId, startDate, endDate);
        return medicalRecordRepository.findByPatientIdAndDateRange(patientId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecord> getMedicalRecordsByPatientIdAndRecordType(
            Long patientId, String recordType) {
        log.debug("Fetching medical records for patient ID: {} with record type: {}", 
                patientId, recordType);
        return medicalRecordRepository.findByPatientIdAndRecordType(patientId, recordType);
    }
}