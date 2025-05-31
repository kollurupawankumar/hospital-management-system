package com.ingestion.nursing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "care_plan_goals")
public class CarePlanGoal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_plan_id", nullable = false)
    private CarePlan carePlan;

    @Column(name = "goal_description", length = 1000, nullable = false)
    private String goalDescription;

    @Column(name = "target_date")
    private LocalDateTime targetDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GoalStatus status = GoalStatus.IN_PROGRESS;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private GoalPriority priority = GoalPriority.MEDIUM;

    @Column(name = "outcome_criteria", length = 1000)
    private String outcomeCriteria;

    @Column(name = "progress_notes", length = 1000)
    private String progressNotes;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by")
    private User completedBy;

    public enum GoalStatus {
        IN_PROGRESS, ACHIEVED, NOT_ACHIEVED, DISCONTINUED
    }

    public enum GoalPriority {
        HIGH, MEDIUM, LOW
    }

    // Getters and Setters
    public CarePlan getCarePlan() {
        return carePlan;
    }

    public void setCarePlan(CarePlan carePlan) {
        this.carePlan = carePlan;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public GoalPriority getPriority() {
        return priority;
    }

    public void setPriority(GoalPriority priority) {
        this.priority = priority;
    }

    public String getOutcomeCriteria() {
        return outcomeCriteria;
    }

    public void setOutcomeCriteria(String outcomeCriteria) {
        this.outcomeCriteria = outcomeCriteria;
    }

    public String getProgressNotes() {
        return progressNotes;
    }

    public void setProgressNotes(String progressNotes) {
        this.progressNotes = progressNotes;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public User getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(User completedBy) {
        this.completedBy = completedBy;
    }

    // Helper methods
    public void markAsAchieved(User user, String notes) {
        this.status = GoalStatus.ACHIEVED;
        this.completionDate = LocalDateTime.now();
        this.completedBy = user;
        this.progressNotes = notes;
    }

    public void markAsNotAchieved(User user, String notes) {
        this.status = GoalStatus.NOT_ACHIEVED;
        this.completionDate = LocalDateTime.now();
        this.completedBy = user;
        this.progressNotes = notes;
    }

    public void discontinue(User user, String notes) {
        this.status = GoalStatus.DISCONTINUED;
        this.completionDate = LocalDateTime.now();
        this.completedBy = user;
        this.progressNotes = notes;
    }

    public void updateProgress(String notes) {
        this.progressNotes = notes;
    }

    public boolean isInProgress() {
        return this.status == GoalStatus.IN_PROGRESS;
    }

    public boolean isAchieved() {
        return this.status == GoalStatus.ACHIEVED;
    }

    public boolean isNotAchieved() {
        return this.status == GoalStatus.NOT_ACHIEVED;
    }

    public boolean isDiscontinued() {
        return this.status == GoalStatus.DISCONTINUED;
    }

    public boolean isOverdue() {
        if (this.status != GoalStatus.IN_PROGRESS) {
            return false;
        }
        
        if (this.targetDate == null) {
            return false;
        }
        
        return LocalDateTime.now().isAfter(this.targetDate);
    }
}