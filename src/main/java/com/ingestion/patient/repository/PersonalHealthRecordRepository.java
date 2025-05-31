package com.ingestion.patient.repository;

import com.ingestion.patient.model.PersonalHealthRecord;
import com.ingestion.patient.model.PersonalHealthRecord.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonalHealthRecordRepository extends JpaRepository<PersonalHealthRecord, Long> {
    
    List<PersonalHealthRecord> findByPatientId(Long patientId);
    
    @Query("SELECT phr FROM PersonalHealthRecord phr WHERE phr.patient.id = :patientId ORDER BY phr.recordDate DESC")
    List<PersonalHealthRecord> findByPatientIdOrderByDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT phr FROM PersonalHealthRecord phr WHERE phr.patient.id = :patientId AND phr.recordType = :recordType ORDER BY phr.recordDate DESC")
    List<PersonalHealthRecord> findByPatientIdAndRecordType(
            @Param("patientId") Long patientId,
            @Param("recordType") RecordType recordType);
    
    @Query("SELECT phr FROM PersonalHealthRecord phr WHERE phr.patient.id = :patientId AND phr.recordDate BETWEEN :startDate AND :endDate ORDER BY phr.recordDate DESC")
    List<PersonalHealthRecord> findByPatientIdAndDateRange(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT phr FROM PersonalHealthRecord phr WHERE phr.patient.id = :patientId AND phr.isSharedWithDoctor = true ORDER BY phr.recordDate DESC")
    List<PersonalHealthRecord> findSharedRecordsByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT DISTINCT phr.recordType FROM PersonalHealthRecord phr WHERE phr.patient.id = :patientId")
    List<RecordType> findRecordTypesByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT phr FROM PersonalHealthRecord phr WHERE phr.patient.id = :patientId AND phr.title LIKE %:searchTerm% OR phr.description LIKE %:searchTerm% ORDER BY phr.recordDate DESC")
    List<PersonalHealthRecord> searchRecordsByPatientId(
            @Param("patientId") Long patientId,
            @Param("searchTerm") String searchTerm);
}