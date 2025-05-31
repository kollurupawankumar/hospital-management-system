package com.ingestion.nursing.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.NursingTask;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NursingTaskService {
    
    NursingTask saveNursingTask(NursingTask nursingTask);
    
    Optional<NursingTask> findById(Long id);
    
    List<NursingTask> findByPatient(Patient patient);
    
    Page<NursingTask> findByPatient(Patient patient, Pageable pageable);
    
    List<NursingTask> findByPatientAndStatus(Patient patient, NursingTask.TaskStatus status);
    
    List<NursingTask> findByPatientAndTaskType(Patient patient, NursingTask.TaskType taskType);
    
    List<NursingTask> findByOrderedBy(Doctor doctor);
    
    List<NursingTask> findByAssignedTo(User nurse);
    
    List<NursingTask> findByCompletedBy(User nurse);
    
    List<NursingTask> findByStatusAndTimeRange(NursingTask.TaskStatus status, LocalDateTime startTime, LocalDateTime endTime);
    
    List<NursingTask> findByCarePlan(CarePlan carePlan);
    
    List<NursingTask> findCarePlanTasks();
    
    List<NursingTask> findDueTasks();
    
    List<NursingTask> findUpcomingTasks(int hoursAhead);
    
    List<NursingTask> findOverdueTasks();
    
    List<NursingTask> findHighPriorityTasks();
    
    NursingTask createTask(Patient patient, NursingTask.TaskType taskType, String taskDescription,
                         NursingTask.TaskPriority priority, LocalDateTime scheduledTime, LocalDateTime dueTime,
                         Doctor orderedBy, User assignedTo, Boolean isRecurring, String recurrencePattern,
                         CarePlan carePlan, Boolean isCarePlanTask);
    
    NursingTask startTask(Long taskId, User nurse);
    
    NursingTask completeTask(Long taskId, User nurse, String notes);
    
    NursingTask cancelTask(Long taskId, String reason);
    
    NursingTask assignTask(Long taskId, User nurse);
    
    void updateTaskStatuses();
    
    void deleteTask(Long id);
}