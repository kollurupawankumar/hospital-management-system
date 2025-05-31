package com.ingestion.doctor.service;

import com.ingestion.doctor.dto.TreatmentActivityDTO;
import com.ingestion.doctor.dto.TreatmentPlanDTO;
import com.ingestion.doctor.model.TreatmentActivity;
import com.ingestion.doctor.model.TreatmentPlan;
import com.ingestion.doctor.repository.DiagnosisRepository;
import com.ingestion.doctor.repository.TreatmentPlanRepository;
import com.ingestion.patient.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TreatmentPlanService {

    private static final Logger log = LoggerFactory.getLogger(TreatmentPlanService.class);
    private final TreatmentPlanRepository treatmentPlanRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    
    public TreatmentPlanService(
            TreatmentPlanRepository treatmentPlanRepository,
            DiagnosisRepository diagnosisRepository,
            PatientService patientService,
            DoctorService doctorService) {
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlan> getAllTreatmentPlans() {
        log.debug("Fetching all treatment plans");
        return treatmentPlanRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TreatmentPlan> getTreatmentPlanById(Long id) {
        log.debug("Fetching treatment plan with ID: {}", id);
        return treatmentPlanRepository.findById(id);
    }

    @Transactional
    public TreatmentPlan saveTreatmentPlan(TreatmentPlan treatmentPlan) {
        log.debug("Saving treatment plan: {}", treatmentPlan);
        return treatmentPlanRepository.save(treatmentPlan);
    }

    @Transactional
    public void deleteTreatmentPlan(Long id) {
        log.debug("Deleting treatment plan with ID: {}", id);
        treatmentPlanRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlan> getTreatmentPlansByPatientId(Long patientId) {
        log.debug("Fetching treatment plans for patient ID: {}", patientId);
        return treatmentPlanRepository.findByPatientIdOrderByStartDateDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlan> getTreatmentPlansByDoctorId(Long doctorId) {
        log.debug("Fetching treatment plans by doctor ID: {}", doctorId);
        return treatmentPlanRepository.findByDoctorIdOrderByStartDateDesc(doctorId);
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlan> getActiveTreatmentPlansByPatientId(Long patientId) {
        log.debug("Fetching active treatment plans for patient ID: {}", patientId);
        LocalDate today = LocalDate.now();
        return treatmentPlanRepository.findByPatientIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                patientId, today, today);
    }

    @Transactional
    public TreatmentPlan createTreatmentPlan(Long patientId, Long doctorId, TreatmentPlanDTO treatmentPlanDTO) {
        log.debug("Creating treatment plan for patient ID: {} by doctor ID: {}", patientId, doctorId);
        
        return patientService.getPatientById(patientId)
            .flatMap(patient -> doctorService.getDoctorById(doctorId)
                .map(doctor -> {
                    TreatmentPlan treatmentPlan = new TreatmentPlan();
                    treatmentPlan.setPatient(patient);
                    treatmentPlan.setDoctor(doctor);
                    treatmentPlan.setPlanName(treatmentPlanDTO.getPlanName());
                    treatmentPlan.setPlanDescription(treatmentPlanDTO.getPlanDescription());
                    treatmentPlan.setStartDate(Optional.ofNullable(treatmentPlanDTO.getStartDate())
                        .orElse(LocalDate.now()));
                    treatmentPlan.setEndDate(treatmentPlanDTO.getEndDate());
                    
                    // Set diagnosis if provided
                    if (treatmentPlanDTO.getDiagnosisId() != null) {
                        diagnosisRepository.findById(treatmentPlanDTO.getDiagnosisId())
                            .ifPresent(treatmentPlan::setDiagnosis);
                    }
                    
                    treatmentPlan.setGoals(treatmentPlanDTO.getGoals());
                    treatmentPlan.setInstructions(treatmentPlanDTO.getInstructions());
                    treatmentPlan.setMedications(treatmentPlanDTO.getMedications());
                    treatmentPlan.setActivities(treatmentPlanDTO.getRecommendedActivities());
                    treatmentPlan.setDietaryRestrictions(treatmentPlanDTO.getDietaryRestrictions());
                    treatmentPlan.setFollowUpInstructions(treatmentPlanDTO.getFollowUpInstructions());
                    treatmentPlan.setNotes(treatmentPlanDTO.getNotes());
                    
                    final TreatmentPlan savedPlan = treatmentPlanRepository.save(treatmentPlan);
                    
                    // Add treatment activities if provided
                    if (treatmentPlanDTO.getActivities() != null && !treatmentPlanDTO.getActivities().isEmpty()) {
                        List<TreatmentActivity> activities = IntStream.range(0, treatmentPlanDTO.getActivities().size())
                            .mapToObj(i -> {
                                TreatmentActivityDTO activityDTO = treatmentPlanDTO.getActivities().get(i);
                                TreatmentActivity activity = new TreatmentActivity();
                                activity.setTreatmentPlan(savedPlan);
                                activity.setActivityName(activityDTO.getActivityName());
                                activity.setActivityDescription(activityDTO.getActivityDescription());
                                activity.setFrequency(activityDTO.getFrequency());
                                activity.setDuration(activityDTO.getDuration());
                                activity.setInstructions(activityDTO.getInstructions());
                                activity.setOrderIndex(i);
                                return activity;
                            })
                            .collect(Collectors.toList());
                        
                        savedPlan.setTreatmentActivities(activities);
                        treatmentPlanRepository.save(savedPlan);
                    }
                    
                    return savedPlan;
                }))
            .orElseThrow(() -> {
                log.error("Patient ID: {} or Doctor ID: {} not found", patientId, doctorId);
                return new IllegalArgumentException("Patient or Doctor not found");
            });
    }

    @Transactional(readOnly = true)
    public List<TreatmentPlanDTO> mapToDTOs(List<TreatmentPlan> treatmentPlans) {
        return treatmentPlans.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public TreatmentPlanDTO mapToDTO(TreatmentPlan treatmentPlan) {
        TreatmentPlanDTO dto = new TreatmentPlanDTO();
        dto.setId(treatmentPlan.getId());
        dto.setPatientId(treatmentPlan.getPatient().getId());
        dto.setDoctorId(treatmentPlan.getDoctor().getId());
        dto.setPatientName(treatmentPlan.getPatient().getFirstName() + " " + treatmentPlan.getPatient().getLastName());
        dto.setDoctorName(treatmentPlan.getDoctor().getFirstName() + " " + treatmentPlan.getDoctor().getLastName());
        dto.setPlanName(treatmentPlan.getPlanName());
        dto.setPlanDescription(treatmentPlan.getPlanDescription());
        dto.setStartDate(treatmentPlan.getStartDate());
        dto.setEndDate(treatmentPlan.getEndDate());
        
        if (treatmentPlan.getDiagnosis() != null) {
            dto.setDiagnosisId(treatmentPlan.getDiagnosis().getId());
            dto.setDiagnosisName(treatmentPlan.getDiagnosis().getDiagnosisName());
        }
        
        dto.setGoals(treatmentPlan.getGoals());
        dto.setInstructions(treatmentPlan.getInstructions());
        dto.setMedications(treatmentPlan.getMedications());
        dto.setRecommendedActivities(treatmentPlan.getActivities());
        dto.setDietaryRestrictions(treatmentPlan.getDietaryRestrictions());
        dto.setFollowUpInstructions(treatmentPlan.getFollowUpInstructions());
        dto.setNotes(treatmentPlan.getNotes());
        
        // Map treatment activities
        if (treatmentPlan.getTreatmentActivities() != null) {
            List<TreatmentActivityDTO> activityDTOs = treatmentPlan.getTreatmentActivities().stream()
                .sorted((a1, a2) -> Integer.compare(
                    Optional.ofNullable(a1.getOrderIndex()).orElse(0),
                    Optional.ofNullable(a2.getOrderIndex()).orElse(0)))
                .map(activity -> {
                    TreatmentActivityDTO activityDTO = new TreatmentActivityDTO();
                    activityDTO.setId(activity.getId());
                    activityDTO.setActivityName(activity.getActivityName());
                    activityDTO.setActivityDescription(activity.getActivityDescription());
                    activityDTO.setFrequency(activity.getFrequency());
                    activityDTO.setDuration(activity.getDuration());
                    activityDTO.setInstructions(activity.getInstructions());
                    activityDTO.setOrderIndex(activity.getOrderIndex());
                    return activityDTO;
                })
                .collect(Collectors.toList());
            
            dto.setActivities(activityDTOs);
        } else {
            dto.setActivities(Collections.emptyList());
        }
        
        return dto;
    }
}