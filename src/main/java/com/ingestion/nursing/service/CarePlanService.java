package com.ingestion.nursing.service;

import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.CarePlanGoal;
import com.ingestion.nursing.model.CarePlanIntervention;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarePlanService {
    
    CarePlan saveCarePlan(CarePlan carePlan);
    
    Optional<CarePlan> findById(Long id);
    
    List<CarePlan> findByPatient(Patient patient);
    
    Page<CarePlan> findByPatient(Patient patient, Pageable pageable);
    
    List<CarePlan> findByPatientAndStatus(Patient patient, CarePlan.CarePlanStatus status);
    
    List<CarePlan> findByCreatedBy(User nurse);
    
    List<CarePlan> findByStatusAndDateRange(CarePlan.CarePlanStatus status, LocalDateTime startDate, LocalDateTime endDate);
    
    List<CarePlan> findActiveCarePlansForPatient(Patient patient);
    
    List<CarePlan> findCurrentActiveCarePlans();
    
    CarePlan createCarePlan(Patient patient, String title, String description, LocalDateTime startDate, User createdBy);
    
    CarePlan completeCarePlan(Long carePlanId, User user);
    
    CarePlan discontinueCarePlan(Long carePlanId, User user);
    
    CarePlanGoal addGoal(Long carePlanId, String goalDescription, LocalDateTime targetDate, 
                       CarePlanGoal.GoalPriority priority, String outcomeCriteria);
    
    CarePlanGoal updateGoalStatus(Long goalId, CarePlanGoal.GoalStatus status, User user, String notes);
    
    CarePlanIntervention addIntervention(Long carePlanId, Long goalId, String interventionDescription, 
                                      String frequency, LocalDateTime startDate, User createdBy);
    
    CarePlanIntervention updateInterventionStatus(Long interventionId, CarePlanIntervention.InterventionStatus status, 
                                               User user, String notes);
    
    void deleteCarePlan(Long id);
    
    void deleteGoal(Long goalId);
    
    void deleteIntervention(Long interventionId);
}