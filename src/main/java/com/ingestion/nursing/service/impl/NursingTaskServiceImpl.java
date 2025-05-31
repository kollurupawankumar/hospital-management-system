package com.ingestion.nursing.service.impl;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.NursingTask;
import com.ingestion.nursing.repository.NursingTaskRepository;
import com.ingestion.nursing.service.NursingTaskService;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NursingTaskServiceImpl implements NursingTaskService {

    @Autowired
    private NursingTaskRepository nursingTaskRepository;

    @Override
    public NursingTask saveNursingTask(NursingTask nursingTask) {
        return nursingTaskRepository.save(nursingTask);
    }

    @Override
    public Optional<NursingTask> findById(Long id) {
        return nursingTaskRepository.findById(id);
    }

    @Override
    public List<NursingTask> findByPatient(Patient patient) {
        return nursingTaskRepository.findByPatientOrderByDueTimeAsc(patient);
    }

    @Override
    public Page<NursingTask> findByPatient(Patient patient, Pageable pageable) {
        return nursingTaskRepository.findByPatientOrderByDueTimeAsc(patient, pageable);
    }

    @Override
    public List<NursingTask> findByPatientAndStatus(Patient patient, NursingTask.TaskStatus status) {
        return nursingTaskRepository.findByPatientAndStatusOrderByDueTimeAsc(patient, status);
    }

    @Override
    public List<NursingTask> findByPatientAndTaskType(Patient patient, NursingTask.TaskType taskType) {
        return nursingTaskRepository.findByPatientAndTaskTypeOrderByDueTimeAsc(patient, taskType);
    }

    @Override
    public List<NursingTask> findByOrderedBy(Doctor doctor) {
        return nursingTaskRepository.findByOrderedByOrderByDueTimeAsc(doctor);
    }

    @Override
    public List<NursingTask> findByAssignedTo(User nurse) {
        return nursingTaskRepository.findByAssignedToOrderByDueTimeAsc(nurse);
    }

    @Override
    public List<NursingTask> findByCompletedBy(User nurse) {
        return nursingTaskRepository.findByCompletedByOrderByCompletedTimeDesc(nurse);
    }

    @Override
    public List<NursingTask> findByStatusAndTimeRange(NursingTask.TaskStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        return nursingTaskRepository.findByStatusAndDueTimeBetweenOrderByDueTimeAsc(status, startTime, endTime);
    }

    @Override
    public List<NursingTask> findByCarePlan(CarePlan carePlan) {
        return nursingTaskRepository.findByCarePlanOrderByDueTimeAsc(carePlan);
    }

    @Override
    public List<NursingTask> findCarePlanTasks() {
        return nursingTaskRepository.findByIsCarePlanTaskTrueOrderByDueTimeAsc();
    }

    @Override
    public List<NursingTask> findDueTasks() {
        LocalDateTime now = LocalDateTime.now();
        return nursingTaskRepository.findDueTasks(now);
    }

    @Override
    public List<NursingTask> findUpcomingTasks(int hoursAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureTime = now.plusHours(hoursAhead);
        return nursingTaskRepository.findUpcomingTasks(now, futureTime);
    }

    @Override
    public List<NursingTask> findOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        return nursingTaskRepository.findOverdueTasks(now);
    }

    @Override
    public List<NursingTask> findHighPriorityTasks() {
        return nursingTaskRepository.findHighPriorityTasks();
    }

    @Override
    public NursingTask createTask(Patient patient, NursingTask.TaskType taskType, String taskDescription,
                                NursingTask.TaskPriority priority, LocalDateTime scheduledTime, LocalDateTime dueTime,
                                Doctor orderedBy, User assignedTo, Boolean isRecurring, String recurrencePattern,
                                CarePlan carePlan, Boolean isCarePlanTask) {
        NursingTask task = new NursingTask();
        task.setPatient(patient);
        task.setTaskType(taskType);
        task.setTaskDescription(taskDescription);
        task.setPriority(priority);
        task.setScheduledTime(scheduledTime);
        task.setDueTime(dueTime);
        task.setOrderedBy(orderedBy);
        task.setAssignedTo(assignedTo);
        task.setIsRecurring(isRecurring);
        task.setRecurrencePattern(recurrencePattern);
        task.setCarePlan(carePlan);
        task.setIsCarePlanTask(isCarePlanTask);
        task.setStatus(NursingTask.TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        
        return nursingTaskRepository.save(task);
    }

    @Override
    public NursingTask startTask(Long taskId, User nurse) {
        Optional<NursingTask> taskOpt = nursingTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            NursingTask task = taskOpt.get();
            task.startTask(nurse);
            return nursingTaskRepository.save(task);
        }
        throw new RuntimeException("Task not found with id: " + taskId);
    }

    @Override
    public NursingTask completeTask(Long taskId, User nurse, String notes) {
        Optional<NursingTask> taskOpt = nursingTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            NursingTask task = taskOpt.get();
            task.completeTask(nurse, notes);
            return nursingTaskRepository.save(task);
        }
        throw new RuntimeException("Task not found with id: " + taskId);
    }

    @Override
    public NursingTask cancelTask(Long taskId, String reason) {
        Optional<NursingTask> taskOpt = nursingTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            NursingTask task = taskOpt.get();
            task.cancelTask(reason);
            return nursingTaskRepository.save(task);
        }
        throw new RuntimeException("Task not found with id: " + taskId);
    }

    @Override
    public NursingTask assignTask(Long taskId, User nurse) {
        Optional<NursingTask> taskOpt = nursingTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            NursingTask task = taskOpt.get();
            task.setAssignedTo(nurse);
            return nursingTaskRepository.save(task);
        }
        throw new RuntimeException("Task not found with id: " + taskId);
    }

    @Override
    public void updateTaskStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<NursingTask> overdueTasks = nursingTaskRepository.findOverdueTasks(now);
        for (NursingTask task : overdueTasks) {
            task.checkAndUpdateStatus();
            nursingTaskRepository.save(task);
        }
    }

    @Override
    public void deleteTask(Long id) {
        nursingTaskRepository.deleteById(id);
    }
}