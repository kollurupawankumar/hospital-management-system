package com.ingestion.doctor.service;

import com.ingestion.common.model.inpatient.InpatientAdmission;
import com.ingestion.common.repository.InpatientAdmissionRepository;
import com.ingestion.doctor.dto.DischargeSummaryDTO;
import com.ingestion.doctor.dto.InpatientNoteDTO;
import com.ingestion.doctor.dto.RoundNoteDTO;
import com.ingestion.doctor.model.*;
import com.ingestion.doctor.repository.*;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.patient.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InpatientCareService {

    private static final Logger log = LoggerFactory.getLogger(InpatientCareService.class);

    @Autowired
    private InpatientAdmissionRepository admissionRepository;
    @Autowired
    private  InpatientNoteRepository inpatientNoteRepository;
    @Autowired
    private  RoundNoteRepository roundNoteRepository;
    @Autowired
    private  DischargeSummaryRepository dischargeSummaryRepository;
    @Autowired
    private  DiagnosisRepository diagnosisRepository;
    @Autowired
    private  PatientService patientService;
    @Autowired
    private  DoctorService doctorService;

    @Transactional(readOnly = true)
    public List<InpatientAdmission> getCurrentInpatients() {
        log.debug("Fetching current inpatients");
        return admissionRepository.findAllCurrentAdmissions();
    }

    @Transactional(readOnly = true)
    public Optional<InpatientAdmission> getCurrentAdmissionForPatient(Long patientId) {
        log.debug("Fetching current admission for patient ID: {}", patientId);
        return admissionRepository.findCurrentAdmissionByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<InpatientAdmission> getAdmissionHistoryForPatient(Long patientId) {
        log.debug("Fetching admission history for patient ID: {}", patientId);
        return patientService.getPatientById(patientId)
            .map(admissionRepository::findByPatientOrderByAdmissionDateDesc)
            .orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<InpatientNote> getInpatientNotesByPatientId(Long patientId) {
        log.debug("Fetching inpatient notes for patient ID: {}", patientId);
        return inpatientNoteRepository.findByPatientIdOrderByNoteDateTimeDesc(patientId);
    }

    @Transactional(readOnly = true)
    public Optional<InpatientNote> getInpatientNoteById(Long id) {
        log.debug("Fetching inpatient note with ID: {}", id);
        return inpatientNoteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<RoundNote> getRoundNotesByPatientId(Long patientId) {
        log.debug("Fetching round notes for patient ID: {}", patientId);
        return roundNoteRepository.findByPatientIdOrderByRoundDateTimeDesc(patientId);
    }

    @Transactional(readOnly = true)
    public Optional<RoundNote> getRoundNoteById(Long id) {
        log.debug("Fetching round note with ID: {}", id);
        return roundNoteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<DischargeSummary> getDischargeSummariesByPatientId(Long patientId) {
        log.debug("Fetching discharge summaries for patient ID: {}", patientId);
        return dischargeSummaryRepository.findByPatientIdOrderByDischargeDateDesc(patientId);
    }

    @Transactional(readOnly = true)
    public Optional<DischargeSummary> getDischargeSummaryById(Long id) {
        log.debug("Fetching discharge summary with ID: {}", id);
        return dischargeSummaryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Diagnosis> getDiagnosesByPatientId(Long patientId) {
        log.debug("Fetching diagnoses for patient ID: {}", patientId);
        return diagnosisRepository.findByPatientIdOrderByDiagnosisDateDesc(patientId);
    }

    @Transactional
    public InpatientNote createInpatientNoteFromDTO(InpatientNoteDTO noteDTO) {
        log.debug("Creating inpatient note from DTO for patient ID: {} by doctor ID: {}", 
                noteDTO.getPatientId(), noteDTO.getDoctorId());
        
        return patientService.getPatientById(noteDTO.getPatientId())
            .flatMap(patient -> doctorService.getDoctorById(noteDTO.getDoctorId())
                .map(doctor -> {
                    InpatientNote note = new InpatientNote();
                    note.setPatient(patient);
                    note.setDoctor(doctor);
                    
                    // Set admission if patient is currently admitted
                    admissionRepository.findCurrentAdmissionByPatientId(patient.getId())
                        .ifPresent(note::setAdmission);
                    
                    note.setNoteDateTime(Optional.ofNullable(noteDTO.getNoteDateTime())
                        .orElse(LocalDateTime.now()));
                    note.setNoteType(noteDTO.getNoteType());
                    note.setNoteContent(noteDTO.getNoteContent());
                    note.setVitalSigns(noteDTO.getVitalSigns());
                    note.setMedications(noteDTO.getMedications());
                    note.setProcedures(noteDTO.getProcedures());
                    note.setAssessments(noteDTO.getAssessments());
                    note.setPlans(noteDTO.getPlans());
                    
                    return inpatientNoteRepository.save(note);
                }))
            .orElseThrow(() -> {
                log.error("Patient ID: {} or Doctor ID: {} not found", 
                        noteDTO.getPatientId(), noteDTO.getDoctorId());
                return new IllegalArgumentException("Patient or Doctor not found");
            });
    }

    @Transactional
    public RoundNote createRoundNoteFromDTO(RoundNoteDTO roundDTO) {
        log.debug("Creating round note from DTO for patient ID: {} by doctor ID: {}", 
                roundDTO.getPatientId(), roundDTO.getDoctorId());
        
        return patientService.getPatientById(roundDTO.getPatientId())
            .flatMap(patient -> doctorService.getDoctorById(roundDTO.getDoctorId())
                .map(doctor -> {
                    RoundNote note = new RoundNote();
                    note.setPatient(patient);
                    note.setDoctor(doctor);
                    
                    // Set admission if patient is currently admitted
                    admissionRepository.findCurrentAdmissionByPatientId(patient.getId())
                        .ifPresent(note::setAdmission);
                    
                    note.setRoundDateTime(Optional.ofNullable(roundDTO.getRoundDateTime())
                        .orElse(LocalDateTime.now()));
                    note.setSubjectiveNotes(roundDTO.getSubjectiveNotes());
                    note.setObjectiveNotes(roundDTO.getObjectiveNotes());
                    note.setVitalSigns(roundDTO.getVitalSigns());
                    note.setAssessment(roundDTO.getAssessment());
                    note.setPlan(roundDTO.getPlan());
                    note.setMedicationChanges(roundDTO.getMedicationChanges());
                    note.setDietaryChanges(roundDTO.getDietaryChanges());
                    note.setActivityChanges(roundDTO.getActivityChanges());
                    note.setAdditionalInstructions(roundDTO.getAdditionalInstructions());
                    
                    return roundNoteRepository.save(note);
                }))
            .orElseThrow(() -> {
                log.error("Patient ID: {} or Doctor ID: {} not found", 
                        roundDTO.getPatientId(), roundDTO.getDoctorId());
                return new IllegalArgumentException("Patient or Doctor not found");
            });
    }

    @Transactional
    public DischargeSummary createDischargeSummaryFromDTO(DischargeSummaryDTO summaryDTO) {
        log.debug("Creating discharge summary from DTO for patient ID: {} by doctor ID: {}", 
                summaryDTO.getPatientId(), summaryDTO.getDoctorId());
        
        return patientService.getPatientById(summaryDTO.getPatientId())
            .flatMap(patient -> doctorService.getDoctorById(summaryDTO.getDoctorId())
                .map(doctor -> {
                    DischargeSummary summary = new DischargeSummary();
                    summary.setPatient(patient);
                    summary.setDoctor(doctor);
                    
                    // Get current admission and update its status
                    Optional<InpatientAdmission> currentAdmission = 
                            admissionRepository.findCurrentAdmissionByPatientId(patient.getId());
                    
                    currentAdmission.ifPresent(admission -> {
                        summary.setAdmission(admission);
                        summary.setAdmissionDate(admission.getAdmissionDate().toLocalDate());
                        
                        // Update admission status to DISCHARGED
                        LocalDateTime now = LocalDateTime.now();
                        admission.setAdmissionStatus(InpatientAdmission.AdmissionStatus.DISCHARGED);
                        admission.setDischargeDate(now.toLocalDate());
                        admission.setActualDischargeDate(now);
                        admission.setDischargeNotes(summaryDTO.getBriefHospitalCourse());
                        admissionRepository.save(admission);
                    });
                    
                    if (!currentAdmission.isPresent()) {
                        summary.setAdmissionDate(Optional.ofNullable(summaryDTO.getAdmissionDate())
                            .orElse(LocalDate.now().minusDays(1)));
                    }
                    
                    summary.setDischargeDate(Optional.ofNullable(summaryDTO.getDischargeDate())
                        .orElse(LocalDate.now()));
                    summary.setAdmissionDiagnosis(summaryDTO.getAdmissionDiagnosis());
                    summary.setDischargeDiagnosis(summaryDTO.getDischargeDiagnosis());
                    summary.setBriefHospitalCourse(summaryDTO.getBriefHospitalCourse());
                    summary.setProceduresPerformed(summaryDTO.getProceduresPerformed());
                    summary.setSignificantFindings(summaryDTO.getSignificantFindings());
                    summary.setConditionAtDischarge(summaryDTO.getConditionAtDischarge());
                    summary.setDischargeMedications(summaryDTO.getDischargeMedications());
                    summary.setDietaryInstructions(summaryDTO.getDietaryInstructions());
                    summary.setActivityInstructions(summaryDTO.getActivityInstructions());
                    summary.setFollowUpInstructions(summaryDTO.getFollowUpInstructions());
                    summary.setAdditionalInstructions(summaryDTO.getAdditionalInstructions());
                    
                    return dischargeSummaryRepository.save(summary);
                }))
            .orElseThrow(() -> {
                log.error("Patient ID: {} or Doctor ID: {} not found", 
                        summaryDTO.getPatientId(), summaryDTO.getDoctorId());
                return new IllegalArgumentException("Patient or Doctor not found");
            });
    }

    @Transactional
    public InpatientAdmission admitPatient(Long patientId, Long doctorId, String admissionDiagnosis, 
                                          String roomNumber, String bedNumber, String ward) {
        log.debug("Admitting patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        return patientService.getPatientById(patientId)
            .flatMap(patient -> doctorService.getDoctorById(doctorId)
                .map(doctor -> {
                    // Check if patient is already admitted
                    Optional<InpatientAdmission> currentAdmission = 
                            admissionRepository.findCurrentAdmissionByPatientId(patientId);
                    
                    if (currentAdmission.isPresent()) {
                        log.warn("Patient ID: {} is already admitted", patientId);
                        throw new IllegalStateException("Patient is already admitted");
                    }
                    
                    InpatientAdmission admission = new InpatientAdmission();
                    admission.setPatient(patient);
                    admission.setAdmittingDoctor(doctor);
                    admission.setAdmissionDate(LocalDateTime.now());
                    admission.setAdmissionDiagnosis(admissionDiagnosis);
                    admission.setRoomNumber(roomNumber);
                    admission.setBedNumber(bedNumber);
                    admission.setWard(ward);
                    admission.setAdmissionStatus(InpatientAdmission.AdmissionStatus.ADMITTED);
                    
                    return admissionRepository.save(admission);
                }))
            .orElseThrow(() -> {
                log.error("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
                return new IllegalArgumentException("Patient or Doctor not found");
            });
    }

    @Transactional(readOnly = true)
    public List<String> getInpatientNoteTypes() {
        return Arrays.asList(
            "Progress Note",
            "Admission Note",
            "Procedure Note",
            "Consultation Note",
            "Nursing Note",
            "Medication Note",
            "Vital Signs",
            "Lab Results Note",
            "Imaging Note",
            "Therapy Note",
            "Dietary Note",
            "Social Work Note",
            "Other"
        );
    }
}