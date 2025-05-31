package com.ingestion.patient.repository;

import com.ingestion.patient.model.Appointment;
import com.ingestion.patient.model.Appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByDoctorId(Long doctorId);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.appointmentDate DESC")
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId ORDER BY a.appointmentDate DESC")
    List<Appointment> findByDoctorIdOrderByAppointmentDateDesc(@Param("doctorId") Long doctorId);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.appointmentDate >= :now ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingAppointmentsByPatientId(@Param("patientId") Long patientId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate >= :now ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingAppointmentsByDoctorId(@Param("doctorId") Long doctorId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.appointmentDate < :now ORDER BY a.appointmentDate DESC")
    List<Appointment> findPastAppointmentsByPatientId(@Param("patientId") Long patientId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate < :now ORDER BY a.appointmentDate DESC")
    List<Appointment> findPastAppointmentsByDoctorId(@Param("doctorId") Long doctorId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate ORDER BY a.appointmentDate ASC")
    List<Appointment> findAppointmentsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate BETWEEN :startDate AND :endDate ORDER BY a.appointmentDate ASC")
    List<Appointment> findAppointmentsByDoctorBetweenDates(@Param("doctorId") Long doctorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Appointment a WHERE a.status = :status ORDER BY a.appointmentDate ASC")
    List<Appointment> findAppointmentsByStatus(@Param("status") AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.isTelemedicine = true AND a.appointmentDate >= :now ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingTelemedicineAppointments(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.isTelemedicine = true AND a.appointmentDate >= :now ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingTelemedicineAppointmentsByPatientId(@Param("patientId") Long patientId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.isTelemedicine = true AND a.appointmentDate >= :now ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingTelemedicineAppointmentsByDoctorId(@Param("doctorId") Long doctorId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate AND a.reminderSent = false")
    List<Appointment> findAppointmentsForReminders(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}