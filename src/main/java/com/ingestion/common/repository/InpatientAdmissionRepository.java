package com.ingestion.common.repository;

import com.ingestion.common.model.inpatient.Bed;
import com.ingestion.common.model.inpatient.InpatientAdmission;
import com.ingestion.common.model.inpatient.Nurse;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InpatientAdmissionRepository extends JpaRepository<InpatientAdmission, Long> {
    
    Optional<InpatientAdmission> findByAdmissionNumber(String admissionNumber);
    
    List<InpatientAdmission> findByPatient(Patient patient);
    
    List<InpatientAdmission> findByAdmittingDoctor(Doctor doctor);
    
    List<InpatientAdmission> findByAssignedNurse(Nurse nurse);
    
    List<InpatientAdmission> findByAssignedBed(Bed bed);
    
    List<InpatientAdmission> findByAdmissionStatus(InpatientAdmission.AdmissionStatus status);
    
    List<InpatientAdmission> findByAdmissionType(InpatientAdmission.AdmissionType type);
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.admissionStatus = 'ADMITTED'")
    List<InpatientAdmission> findAllCurrentAdmissions();
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.admissionStatus = 'ADMITTED' AND a.admittingDoctor = ?1")
    List<InpatientAdmission> findCurrentAdmissionsByDoctor(Doctor doctor);
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.admissionStatus = 'ADMITTED' AND a.assignedNurse = ?1")
    List<InpatientAdmission> findCurrentAdmissionsByNurse(Nurse nurse);
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.admissionStatus = 'ADMITTED' AND a.assignedBed.ward.department.id = ?1")
    List<InpatientAdmission> findCurrentAdmissionsByDepartmentId(Long departmentId);
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.admissionStatus = 'ADMITTED' AND a.assignedBed.ward.id = ?1")
    List<InpatientAdmission> findCurrentAdmissionsByWardId(Long wardId);
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.admissionDate BETWEEN ?1 AND ?2")
    List<InpatientAdmission> findByAdmissionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.actualDischargeDate BETWEEN ?1 AND ?2")
    List<InpatientAdmission> findByDischargeDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.admissionStatus = 'ADMITTED' AND a.expectedDischargeDate < CURRENT_DATE")
    List<InpatientAdmission> findOverdueDischarges();
    
    @Query("SELECT a FROM InpatientAdmission a WHERE a.isEmergency = true AND a.admissionStatus = 'ADMITTED'")
    List<InpatientAdmission> findCurrentEmergencyAdmissions();
    
    // Added methods to fix compilation errors
    @Query("SELECT a FROM InpatientAdmission a WHERE a.patient.id = ?1 AND a.admissionStatus = 'ADMITTED'")
    Optional<InpatientAdmission> findCurrentAdmissionByPatientId(Long patientId);
    
    List<InpatientAdmission> findByPatientOrderByAdmissionDateDesc(Patient patient);
}