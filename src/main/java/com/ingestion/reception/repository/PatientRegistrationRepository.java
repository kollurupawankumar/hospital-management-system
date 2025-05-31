package com.ingestion.reception.repository;

import com.ingestion.reception.model.PatientRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRegistrationRepository extends JpaRepository<PatientRegistration, Long> {
    
    Optional<PatientRegistration> findByRegistrationNumber(String registrationNumber);
    
    List<PatientRegistration> findByPatientId(Long patientId);
    
    @Query("SELECT pr FROM PatientRegistration pr WHERE pr.registrationDate BETWEEN :startDate AND :endDate ORDER BY pr.registrationDate DESC")
    List<PatientRegistration> findByRegistrationDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT pr FROM PatientRegistration pr WHERE pr.registrationType = :type AND pr.registrationDate BETWEEN :startDate AND :endDate ORDER BY pr.registrationDate DESC")
    List<PatientRegistration> findByRegistrationTypeAndDateBetween(
            @Param("type") PatientRegistration.RegistrationType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(pr) FROM PatientRegistration pr WHERE pr.registrationDate BETWEEN :startDate AND :endDate")
    long countRegistrationsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT MAX(pr.registrationNumber) FROM PatientRegistration pr WHERE pr.registrationNumber LIKE CONCAT(:prefix, '%')")
    String findLastRegistrationNumberWithPrefix(@Param("prefix") String prefix);
}