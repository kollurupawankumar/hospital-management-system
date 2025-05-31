package com.ingestion.nursing.repository;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.NursingTask;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NursingTaskRepository extends JpaRepository<NursingTask, Long> {
    
    List<NursingTask> findByPatientOrderByDueTimeAsc(Patient patient);
    
    Page<NursingTask> findByPatientOrderByDueTimeAsc(Patient patient, Pageable pageable);
    
    List<NursingTask> findByPatientAndStatusOrderByDueTimeAsc(
            Patient patient, NursingTask.TaskStatus status);
    
    List<NursingTask> findByPatientAndTaskTypeOrderByDueTimeAsc(
            Patient patient, NursingTask.TaskType taskType);
    
    List<NursingTask> findByOrderedByOrderByDueTimeAsc(Doctor doctor);
    
    List<NursingTask> findByAssignedToOrderByDueTimeAsc(User nurse);
    
    List<NursingTask> findByCompletedByOrderByCompletedTimeDesc(User nurse);
    
    List<NursingTask> findByStatusAndDueTimeBetweenOrderByDueTimeAsc(
            NursingTask.TaskStatus status, LocalDateTime startTime, LocalDateTime endTime);
    
    List<NursingTask> findByCarePlanOrderByDueTimeAsc(CarePlan carePlan);
    
    List<NursingTask> findByIsCarePlanTaskTrueOrderByDueTimeAsc();
    
    @Query("SELECT t FROM NursingTask t WHERE t.status = 'PENDING' AND t.dueTime <= :now ORDER BY t.priority, t.dueTime ASC")
    List<NursingTask> findDueTasks(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM NursingTask t WHERE t.status = 'PENDING' AND t.dueTime BETWEEN :start AND :end ORDER BY t.priority, t.dueTime ASC")
    List<NursingTask> findUpcomingTasks(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT t FROM NursingTask t WHERE t.status = 'PENDING' AND t.dueTime < :now ORDER BY t.priority, t.dueTime ASC")
    List<NursingTask> findOverdueTasks(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM NursingTask t WHERE t.priority IN ('STAT', 'URGENT') AND t.status = 'PENDING' ORDER BY t.priority, t.dueTime ASC")
    List<NursingTask> findHighPriorityTasks();
}