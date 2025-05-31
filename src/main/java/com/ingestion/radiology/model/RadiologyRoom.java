package com.ingestion.radiology.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "radiology_rooms")
public class RadiologyRoom extends BaseEntity {

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "location")
    private String location;

    @Column(name = "description", length = 500)
    private String description;

    @ElementCollection
    @CollectionTable(name = "radiology_room_modalities", 
                    joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "modality")
    @Enumerated(EnumType.STRING)
    private Set<RadiologyProcedure.Modality> supportedModalities = new HashSet<>();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "equipment_details", length = 500)
    private String equipmentDetails;

    // Getters and Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RadiologyProcedure.Modality> getSupportedModalities() {
        return supportedModalities;
    }

    public void setSupportedModalities(Set<RadiologyProcedure.Modality> supportedModalities) {
        this.supportedModalities = supportedModalities;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public String getEquipmentDetails() {
        return equipmentDetails;
    }

    public void setEquipmentDetails(String equipmentDetails) {
        this.equipmentDetails = equipmentDetails;
    }

    // Helper methods
    public void addModality(RadiologyProcedure.Modality modality) {
        supportedModalities.add(modality);
    }

    public void removeModality(RadiologyProcedure.Modality modality) {
        supportedModalities.remove(modality);
    }

    public boolean supportsModality(RadiologyProcedure.Modality modality) {
        return supportedModalities.contains(modality);
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}