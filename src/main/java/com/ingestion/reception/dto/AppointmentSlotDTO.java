package com.ingestion.reception.dto;

import com.ingestion.reception.model.AppointmentSlot;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentSlotDTO {
    
    private Long id;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    private String doctorName;
    
    private String doctorSpecialization;
    
    @NotNull(message = "Slot date is required")
    private LocalDate slotDate;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    private boolean isAvailable = true;
    
    @NotNull(message = "Slot type is required")
    private AppointmentSlot.SlotType slotType;
    
    @Min(value = 1, message = "Maximum patients must be at least 1")
    private int maxPatients = 1;
    
    private int bookedPatients = 0;
    
    private String department;
    
    private String location;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSpecialization() {
        return doctorSpecialization;
    }

    public void setDoctorSpecialization(String doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public AppointmentSlot.SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(AppointmentSlot.SlotType slotType) {
        this.slotType = slotType;
    }

    public int getMaxPatients() {
        return maxPatients;
    }

    public void setMaxPatients(int maxPatients) {
        this.maxPatients = maxPatients;
    }

    public int getBookedPatients() {
        return bookedPatients;
    }

    public void setBookedPatients(int bookedPatients) {
        this.bookedPatients = bookedPatients;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Helper methods
    public boolean hasAvailability() {
        return isAvailable && bookedPatients < maxPatients;
    }
    
    public int getAvailableSlots() {
        return maxPatients - bookedPatients;
    }
    
    public String getTimeRange() {
        return startTime.toString() + " - " + endTime.toString();
    }
}