package com.ingestion.reception.dto;

import com.ingestion.reception.model.QueueManagement;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class QueueManagementDTO {
    
    private Long id;
    
    @NotBlank(message = "Queue name is required")
    private String queueName;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    @NotNull(message = "Queue date is required")
    private LocalDate queueDate;
    
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    @NotNull(message = "Maximum capacity is required")
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    private int maxCapacity;
    
    private int currentCount = 0;
    
    private Integer averageWaitTimeMinutes;
    
    private String currentTokenNumber;
    
    private String nextTokenNumber;
    
    private QueueManagement.QueueStatus queueStatus = QueueManagement.QueueStatus.ACTIVE;
    
    private String counterNumbers;
    
    private String managedBy;
    
    private String notes;
    
    // Additional fields for display
    private String statusDisplay;
    private String capacityDisplay;
    private boolean hasCapacity;
    private String estimatedWaitTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getQueueDate() {
        return queueDate;
    }

    public void setQueueDate(LocalDate queueDate) {
        this.queueDate = queueDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public Integer getAverageWaitTimeMinutes() {
        return averageWaitTimeMinutes;
    }

    public void setAverageWaitTimeMinutes(Integer averageWaitTimeMinutes) {
        this.averageWaitTimeMinutes = averageWaitTimeMinutes;
    }

    public String getCurrentTokenNumber() {
        return currentTokenNumber;
    }

    public void setCurrentTokenNumber(String currentTokenNumber) {
        this.currentTokenNumber = currentTokenNumber;
    }

    public String getNextTokenNumber() {
        return nextTokenNumber;
    }

    public void setNextTokenNumber(String nextTokenNumber) {
        this.nextTokenNumber = nextTokenNumber;
    }

    public QueueManagement.QueueStatus getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(QueueManagement.QueueStatus queueStatus) {
        this.queueStatus = queueStatus;
    }

    public String getCounterNumbers() {
        return counterNumbers;
    }

    public void setCounterNumbers(String counterNumbers) {
        this.counterNumbers = counterNumbers;
    }

    public String getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(String managedBy) {
        this.managedBy = managedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public String getCapacityDisplay() {
        return capacityDisplay;
    }

    public void setCapacityDisplay(String capacityDisplay) {
        this.capacityDisplay = capacityDisplay;
    }

    public boolean isHasCapacity() {
        return hasCapacity;
    }

    public void setHasCapacity(boolean hasCapacity) {
        this.hasCapacity = hasCapacity;
    }

    public String getEstimatedWaitTime() {
        return estimatedWaitTime;
    }

    public void setEstimatedWaitTime(String estimatedWaitTime) {
        this.estimatedWaitTime = estimatedWaitTime;
    }
}