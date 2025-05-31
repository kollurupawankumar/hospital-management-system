package com.ingestion.nursing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nursing_tasks")
public class NursingTask extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Column(name = "task_description", length = 1000, nullable = false)
    private String taskDescription;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.ROUTINE;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "due_time")
    private LocalDateTime dueTime;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordered_by")
    private Doctor orderedBy;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private User completedBy;

    @Column(name = "completion_notes", length = 1000)
    private String completionNotes;

    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @Column(name = "recurrence_pattern")
    private String recurrencePattern;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_plan_id")
    private CarePlan carePlan;

    @Column(name = "is_care_plan_task")
    private Boolean isCarePlanTask = false;

    public enum TaskType {
        VITAL_SIGNS, MEDICATION, ASSESSMENT, PROCEDURE, SPECIMEN_COLLECTION, WOUND_CARE, HYGIENE, MOBILITY, NUTRITION, ELIMINATION, OTHER
    }

    public enum TaskPriority {
        STAT, URGENT, HIGH, ROUTINE, LOW
    }

    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED, CANCELLED, OVERDUE
    }

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public Doctor getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Doctor orderedBy) {
        this.orderedBy = orderedBy;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(User completedBy) {
        this.completedBy = completedBy;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public CarePlan getCarePlan() {
        return carePlan;
    }

    public void setCarePlan(CarePlan carePlan) {
        this.carePlan = carePlan;
    }

    public Boolean getIsCarePlanTask() {
        return isCarePlanTask;
    }

    public void setIsCarePlanTask(Boolean carePlanTask) {
        isCarePlanTask = carePlanTask;
    }

    // Helper methods
    public void startTask(User nurse) {
        this.status = TaskStatus.IN_PROGRESS;
        this.assignedTo = nurse;
    }

    public void completeTask(User nurse, String notes) {
        this.status = TaskStatus.COMPLETED;
        this.completedBy = nurse;
        this.completedTime = LocalDateTime.now();
        this.completionNotes = notes;
    }

    public void cancelTask(String reason) {
        this.status = TaskStatus.CANCELLED;
        this.completionNotes = reason;
    }

    public boolean isOverdue() {
        if (this.status != TaskStatus.PENDING && this.status != TaskStatus.IN_PROGRESS) {
            return false;
        }
        
        if (this.dueTime == null) {
            return false;
        }
        
        return LocalDateTime.now().isAfter(this.dueTime);
    }

    public void checkAndUpdateStatus() {
        if (isOverdue() && this.status != TaskStatus.OVERDUE) {
            this.status = TaskStatus.OVERDUE;
        }
    }

    public boolean isDue(int minutesThreshold) {
        if (this.status != TaskStatus.PENDING) {
            return false;
        }
        
        if (this.scheduledTime == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdTime = this.scheduledTime.minusMinutes(minutesThreshold);
        
        return now.isAfter(thresholdTime) || now.isEqual(thresholdTime);
    }
}