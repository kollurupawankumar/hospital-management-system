package com.ingestion.nursing.repository;

import com.ingestion.nursing.model.MedicationAdministration;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Medication;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.pharmacy.model.PrescriptionItem;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicationAdministrationRepository extends JpaRepository<MedicationAdministration, Long> {
    
    List<MedicationAdministration> findByPatientOrderByScheduledTimeDesc(Patient patient);
    
    Page<MedicationAdministration> findByPatientOrderByScheduledTimeDesc(Patient patient, Pageable pageable);
    
    List<MedicationAdministration> findByPatientAndStatusOrderByScheduledTimeDesc(
            Patient patient, MedicationAdministration.AdministrationStatus status);
    
    List<MedicationAdministration> findByPrescriptionOrderByScheduledTimeDesc(Prescription prescription);
    
    List<MedicationAdministration> findByPrescriptionItemOrderByScheduledTimeDesc(PrescriptionItem prescriptionItem);
    
    List<MedicationAdministration> findByMedicationOrderByScheduledTimeDesc(Medication medication);
    
    List<MedicationAdministration> findByAdministeredByOrderByAdministeredTimeDesc(User nurse);
    
    List<MedicationAdministration> findByStatusAndScheduledTimeBetweenOrderByScheduledTimeAsc(
            MedicationAdministration.AdministrationStatus status, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT ma FROM MedicationAdministration ma WHERE ma.status = 'SCHEDULED' AND ma.scheduledTime <= :now ORDER BY ma.scheduledTime ASC")
    List<MedicationAdministration> findDueMedications(@Param("now") LocalDateTime now);
    
    @Query("SELECT ma FROM MedicationAdministration ma WHERE ma.status = 'SCHEDULED' AND ma.scheduledTime BETWEEN :start AND :end ORDER BY ma.scheduledTime ASC")
    List<MedicationAdministration> findUpcomingMedications(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT ma FROM MedicationAdministration ma WHERE ma.status = 'SCHEDULED' AND ma.scheduledTime < :now ORDER BY ma.scheduledTime ASC")
    List<MedicationAdministration> findOverdueMedications(@Param("now") LocalDateTime now);
    
    @Query("SELECT ma FROM MedicationAdministration ma WHERE ma.isStat = true AND ma.status = 'SCHEDULED' ORDER BY ma.scheduledTime ASC")
    List<MedicationAdministration> findStatMedications();
}