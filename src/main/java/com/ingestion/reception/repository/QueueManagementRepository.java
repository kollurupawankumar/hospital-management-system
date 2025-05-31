package com.ingestion.reception.repository;

import com.ingestion.reception.model.QueueManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QueueManagementRepository extends JpaRepository<QueueManagement, Long> {
    
    List<QueueManagement> findByQueueDate(LocalDate queueDate);
    
    List<QueueManagement> findByQueueDateAndDepartment(LocalDate queueDate, String department);
    
    Optional<QueueManagement> findByQueueNameAndQueueDate(String queueName, LocalDate queueDate);
    
    @Query("SELECT q FROM QueueManagement q WHERE q.queueDate = :date AND q.queueStatus IN ('ACTIVE', 'PAUSED')")
    List<QueueManagement> findActiveQueuesByDate(@Param("date") LocalDate date);
    
    @Query("SELECT q FROM QueueManagement q WHERE q.queueDate = :date AND q.department = :department AND q.queueStatus IN ('ACTIVE', 'PAUSED')")
    List<QueueManagement> findActiveQueuesByDateAndDepartment(
            @Param("date") LocalDate date,
            @Param("department") String department);
    
    @Query("SELECT q FROM QueueManagement q WHERE q.queueDate = :date AND q.queueStatus = 'ACTIVE' AND q.currentCount < q.maxCapacity")
    List<QueueManagement> findQueuesWithAvailabilityByDate(@Param("date") LocalDate date);
    
    @Query("SELECT q FROM QueueManagement q WHERE q.queueDate = :date AND q.department = :department AND q.queueStatus = 'ACTIVE' AND q.currentCount < q.maxCapacity")
    List<QueueManagement> findQueuesWithAvailabilityByDateAndDepartment(
            @Param("date") LocalDate date,
            @Param("department") String department);
    
    @Query("SELECT AVG(q.averageWaitTimeMinutes) FROM QueueManagement q WHERE q.queueDate = :date AND q.department = :department AND q.averageWaitTimeMinutes IS NOT NULL")
    Double calculateAverageWaitTimeForDepartment(
            @Param("date") LocalDate date,
            @Param("department") String department);
}