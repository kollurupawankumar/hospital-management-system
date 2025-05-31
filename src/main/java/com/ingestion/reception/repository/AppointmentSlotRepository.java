package com.ingestion.reception.repository;

import com.ingestion.reception.model.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    
    List<AppointmentSlot> findByDoctorIdAndSlotDate(Long doctorId, LocalDate slotDate);
    
    List<AppointmentSlot> findByDoctorIdAndSlotDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate);
    
    List<AppointmentSlot> findBySlotDateAndIsAvailableTrue(LocalDate slotDate);
    
    List<AppointmentSlot> findBySlotDateBetweenAndIsAvailableTrue(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT a FROM AppointmentSlot a WHERE a.slotDate = :date AND a.slotType = :slotType AND a.isAvailable = true AND a.bookedPatients < a.maxPatients")
    List<AppointmentSlot> findAvailableSlotsByDateAndType(
            @Param("date") LocalDate date,
            @Param("slotType") AppointmentSlot.SlotType slotType);
    
    @Query("SELECT a FROM AppointmentSlot a WHERE a.slotDate = :date AND a.department = :department AND a.isAvailable = true AND a.bookedPatients < a.maxPatients")
    List<AppointmentSlot> findAvailableSlotsByDateAndDepartment(
            @Param("date") LocalDate date,
            @Param("department") String department);
    
    @Query("SELECT a FROM AppointmentSlot a WHERE a.doctor.id = :doctorId AND a.slotDate = :date AND a.startTime <= :time AND a.endTime >= :time")
    List<AppointmentSlot> findSlotsByDoctorAndDateAndTime(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time);
    
    @Query("SELECT COUNT(a) FROM AppointmentSlot a WHERE a.slotDate = :date AND a.isAvailable = true AND a.bookedPatients < a.maxPatients")
    long countAvailableSlotsByDate(@Param("date") LocalDate date);
}