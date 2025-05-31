package com.ingestion.nursing.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "care_plans")
public class CarePlan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CarePlanStatus status = CarePlanStatus.ACTIVE;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_updated_by")
    private User lastUpdatedBy;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarePlanGoal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "carePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarePlanIntervention> interventions = new ArrayList<>();

    @OneToMany(mappedBy = "carePlan")
    private List<NursingTask> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "carePlan")
    private List<NursingNote> notes = new ArrayList<>();

    public enum CarePlanStatus {
        ACTIVE, COMPLETED, DISCONTINUED
    }

    // Getters and Setters
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CarePlanStatus getStatus() {
        return status;
    }

    public void setStatus(CarePlanStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        // Also set the string version for BaseEntity
        setCreatedBy(creator != null ? creator.getUsername() : null);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public List<CarePlanGoal> getGoals() {
        return goals;
    }

    public void setGoals(List<CarePlanGoal> goals) {
        this.goals = goals;
    }

    public List<CarePlanIntervention> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<CarePlanIntervention> interventions) {
        this.interventions = interventions;
    }

    public List<NursingTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<NursingTask> tasks) {
        this.tasks = tasks;
    }

    public List<NursingNote> getNotes() {
        return notes;
    }

    public void setNotes(List<NursingNote> notes) {
        this.notes = notes;
    }

    // Helper methods
    public void addGoal(CarePlanGoal goal) {
        goals.add(goal);
        goal.setCarePlan(this);
    }

    public void removeGoal(CarePlanGoal goal) {
        goals.remove(goal);
        goal.setCarePlan(null);
    }

    public void addIntervention(CarePlanIntervention intervention) {
        interventions.add(intervention);
        intervention.setCarePlan(this);
    }

    public void removeIntervention(CarePlanIntervention intervention) {
        interventions.remove(intervention);
        intervention.setCarePlan(null);
    }

    public void complete(User user) {
        this.status = CarePlanStatus.COMPLETED;
        this.endDate = LocalDateTime.now();
        this.lastUpdatedBy = user;
        this.lastUpdatedDate = LocalDateTime.now();
    }

    public void discontinue(User user) {
        this.status = CarePlanStatus.DISCONTINUED;
        this.endDate = LocalDateTime.now();
        this.lastUpdatedBy = user;
        this.lastUpdatedDate = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == CarePlanStatus.ACTIVE;
    }

    public boolean isCompleted() {
        return this.status == CarePlanStatus.COMPLETED;
    }

    public boolean isDiscontinued() {
        return this.status == CarePlanStatus.DISCONTINUED;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        lastUpdatedDate = LocalDateTime.now();
        // Set the string version of creator for BaseEntity
        if (creator != null) {
            setCreatedBy(creator.getUsername());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = LocalDateTime.now();
    }
}