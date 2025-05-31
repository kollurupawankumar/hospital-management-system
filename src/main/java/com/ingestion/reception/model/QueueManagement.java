package com.ingestion.reception.model;

import com.ingestion.common.model.BaseEntity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "queue_management")
public class QueueManagement extends BaseEntity {

    @Column(name = "queue_name", nullable = false)
    private String queueName;
    
    @Column(name = "department", nullable = false)
    private String department;
    
    @Column(name = "queue_date", nullable = false)
    private LocalDate queueDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;
    
    @Column(name = "current_count", nullable = false)
    private int currentCount = 0;
    
    @Column(name = "average_wait_time_minutes")
    private Integer averageWaitTimeMinutes;
    
    @Column(name = "current_token_number")
    private String currentTokenNumber;
    
    @Column(name = "next_token_number")
    private String nextTokenNumber;
    
    @Column(name = "queue_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private QueueStatus queueStatus = QueueStatus.ACTIVE;
    
    @Column(name = "counter_numbers")
    private String counterNumbers;
    
    @Column(name = "managed_by")
    private String managedBy;
    
    @Column(name = "notes")
    private String notes;
    
    public enum QueueStatus {
        ACTIVE,
        PAUSED,
        CLOSED,
        FULL
    }
    
    public boolean hasCapacity() {
        return currentCount < maxCapacity;
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

    public QueueStatus getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(QueueStatus queueStatus) {
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
}