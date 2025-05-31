package com.ingestion.reception.repository;

import com.ingestion.reception.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    Optional<Token> findByTokenNumber(String tokenNumber);
    
    List<Token> findByPatientId(Long patientId);
    
    List<Token> findByDoctorIdAndTokenDate(Long doctorId, LocalDate tokenDate);
    
    List<Token> findByTokenDateAndDepartmentOrderByIssueTimeAsc(LocalDate tokenDate, String department);
    
    @Query("SELECT t FROM Token t WHERE t.tokenDate = :date AND t.tokenStatus IN ('WAITING', 'CALLED') ORDER BY " +
           "CASE WHEN t.priority = 'URGENT' THEN 0 " +
           "WHEN t.priority = 'HIGH' THEN 1 " +
           "WHEN t.priority = 'NORMAL' THEN 2 " +
           "WHEN t.priority = 'LOW' THEN 3 END, t.issueTime ASC")
    List<Token> findActiveTokensByDateOrderByPriorityAndTime(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM Token t WHERE t.tokenDate = :date AND t.department = :department AND t.tokenStatus IN ('WAITING', 'CALLED') ORDER BY " +
           "CASE WHEN t.priority = 'URGENT' THEN 0 " +
           "WHEN t.priority = 'HIGH' THEN 1 " +
           "WHEN t.priority = 'NORMAL' THEN 2 " +
           "WHEN t.priority = 'LOW' THEN 3 END, t.issueTime ASC")
    List<Token> findActiveTokensByDateAndDepartmentOrderByPriorityAndTime(
            @Param("date") LocalDate date,
            @Param("department") String department);
    
    @Query("SELECT t FROM Token t WHERE t.doctor.id = :doctorId AND t.tokenDate = :date AND t.tokenStatus IN ('WAITING', 'CALLED') ORDER BY " +
           "CASE WHEN t.priority = 'URGENT' THEN 0 " +
           "WHEN t.priority = 'HIGH' THEN 1 " +
           "WHEN t.priority = 'NORMAL' THEN 2 " +
           "WHEN t.priority = 'LOW' THEN 3 END, t.issueTime ASC")
    List<Token> findActiveTokensByDoctorAndDateOrderByPriorityAndTime(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(t) FROM Token t WHERE t.tokenDate = :date AND t.department = :department")
    long countTokensByDateAndDepartment(
            @Param("date") LocalDate date,
            @Param("department") String department);
    
    @Query("SELECT MAX(t.tokenNumber) FROM Token t WHERE t.tokenNumber LIKE CONCAT(:prefix, '%') AND t.tokenDate = :date")
    String findLastTokenNumberWithPrefixForDate(
            @Param("prefix") String prefix,
            @Param("date") LocalDate date);
    
    @Query("SELECT AVG(EXTRACT(EPOCH FROM t.actualServiceTime) - EXTRACT(EPOCH FROM t.issueTime))/60 " +
           "FROM Token t WHERE t.tokenDate = :date AND t.department = :department AND t.actualServiceTime IS NOT NULL")
    Double calculateAverageWaitTimeInMinutes(
            @Param("date") LocalDate date,
            @Param("department") String department);
}