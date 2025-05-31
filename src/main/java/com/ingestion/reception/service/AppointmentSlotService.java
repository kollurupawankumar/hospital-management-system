package com.ingestion.reception.service;

import com.ingestion.reception.dto.AppointmentSlotDTO;
import com.ingestion.reception.model.AppointmentSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentSlotService {
    
    /**
     * Create a new appointment slot
     * @param slotDTO the slot data
     * @return the created slot
     */
    AppointmentSlotDTO createAppointmentSlot(AppointmentSlotDTO slotDTO);
    
    /**
     * Create multiple appointment slots
     * @param slotDTOs list of slot data
     * @return list of created slots
     */
    List<AppointmentSlotDTO> createAppointmentSlots(List<AppointmentSlotDTO> slotDTOs);
    
    /**
     * Get an appointment slot by ID
     * @param id the slot ID
     * @return the slot
     */
    AppointmentSlotDTO getAppointmentSlotById(Long id);
    
    /**
     * Get appointment slots for a doctor on a specific date
     * @param doctorId the doctor ID
     * @param date the date
     * @return list of slots
     */
    List<AppointmentSlotDTO> getSlotsByDoctorAndDate(Long doctorId, LocalDate date);
    
    /**
     * Get appointment slots for a doctor within a date range
     * @param doctorId the doctor ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of slots
     */
    List<AppointmentSlotDTO> getSlotsByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get available appointment slots for a specific date
     * @param date the date
     * @return list of available slots
     */
    List<AppointmentSlotDTO> getAvailableSlotsByDate(LocalDate date);
    
    /**
     * Get available appointment slots within a date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of available slots
     */
    List<AppointmentSlotDTO> getAvailableSlotsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get available slots by date and slot type
     * @param date the date
     * @param slotType the slot type
     * @return list of available slots
     */
    List<AppointmentSlotDTO> getAvailableSlotsByDateAndType(LocalDate date, AppointmentSlot.SlotType slotType);
    
    /**
     * Get available slots by date and department
     * @param date the date
     * @param department the department
     * @return list of available slots
     */
    List<AppointmentSlotDTO> getAvailableSlotsByDateAndDepartment(LocalDate date, String department);
    
    /**
     * Find slots for a doctor at a specific time
     * @param doctorId the doctor ID
     * @param date the date
     * @param time the time
     * @return list of slots
     */
    List<AppointmentSlotDTO> findSlotsByDoctorAndTime(Long doctorId, LocalDate date, LocalTime time);
    
    /**
     * Update an appointment slot
     * @param id the slot ID
     * @param slotDTO the updated slot data
     * @return the updated slot
     */
    AppointmentSlotDTO updateAppointmentSlot(Long id, AppointmentSlotDTO slotDTO);
    
    /**
     * Book a slot (increment booked patients)
     * @param id the slot ID
     * @return the updated slot
     */
    AppointmentSlotDTO bookSlot(Long id);
    
    /**
     * Cancel a booking (decrement booked patients)
     * @param id the slot ID
     * @return the updated slot
     */
    AppointmentSlotDTO cancelBooking(Long id);
    
    /**
     * Delete an appointment slot
     * @param id the slot ID
     */
    void deleteAppointmentSlot(Long id);
    
    /**
     * Count available slots for a specific date
     * @param date the date
     * @return the count of available slots
     */
    long countAvailableSlotsByDate(LocalDate date);
}