package com.ingestion.common.model.inpatient;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wards")
public class Ward extends BaseEntity {

    @NotBlank(message = "Ward name is required")
    @Size(min = 2, max = 100, message = "Ward name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "floor")
    private String floor;

    @NotNull(message = "Total beds is required")
    @Column(name = "total_beds", nullable = false)
    private Integer totalBeds;

    @Column(name = "available_beds")
    private Integer availableBeds;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "ward_type", nullable = false)
    private WardType wardType;

    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (availableBeds == null) {
            availableBeds = totalBeds;
        }
    }

    public enum WardType {
        GENERAL, PRIVATE, SEMI_PRIVATE, ICU, CCU, NICU, PICU, EMERGENCY, ISOLATION, MATERNITY, PEDIATRIC, PSYCHIATRIC, SURGICAL, OTHER
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getTotalBeds() {
        return totalBeds;
    }

    public void setTotalBeds(Integer totalBeds) {
        this.totalBeds = totalBeds;
    }

    public Integer getAvailableBeds() {
        return availableBeds;
    }

    public void setAvailableBeds(Integer availableBeds) {
        this.availableBeds = availableBeds;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public WardType getWardType() {
        return wardType;
    }

    public void setWardType(WardType wardType) {
        this.wardType = wardType;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }

    // Helper method to add a bed
    public void addBed(Bed bed) {
        beds.add(bed);
        bed.setWard(this);
    }

    // Helper method to remove a bed
    public void removeBed(Bed bed) {
        beds.remove(bed);
        bed.setWard(null);
    }

    // Helper method to update available beds count
    public void updateAvailableBeds() {
        int occupied = 0;
        for (Bed bed : beds) {
            if (bed.getStatus() == Bed.BedStatus.OCCUPIED) {
                occupied++;
            }
        }
        this.availableBeds = this.totalBeds - occupied;
    }
}