package com.ingestion.nursing.repository;

import com.ingestion.common.model.inpatient.Department;
import com.ingestion.nursing.model.ShiftHandover;
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
public interface ShiftHandoverRepository extends JpaRepository<ShiftHandover, Long> {
    
    List<ShiftHandover> findByOutgoingNurseOrderByHandoverDateDesc(User nurse);
    
    List<ShiftHandover> findByIncomingNurseOrderByHandoverDateDesc(User nurse);
    
    Page<ShiftHandover> findByDepartmentOrderByHandoverDateDesc(Department department, Pageable pageable);
    
    List<ShiftHandover> findByDepartmentAndHandoverDateBetweenOrderByHandoverDateDesc(
            Department department, LocalDateTime startDate, LocalDateTime endDate);
    
    List<ShiftHandover> findByShiftTypeAndHandoverDateBetweenOrderByHandoverDateDesc(
            ShiftHandover.ShiftType shiftType, LocalDateTime startDate, LocalDateTime endDate);
    
    List<ShiftHandover> findByIsAcknowledgedFalseAndIncomingNurseOrderByHandoverDateDesc(User nurse);
    
    @Query("SELECT sh FROM ShiftHandover sh WHERE sh.handoverDate >= :startOfDay AND sh.handoverDate <= :endOfDay ORDER BY sh.handoverDate DESC")
    List<ShiftHandover> findTodayHandovers(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    
    @Query("SELECT sh FROM ShiftHandover sh WHERE sh.isAcknowledged = false AND sh.handoverDate <= :cutoffTime ORDER BY sh.handoverDate ASC")
    List<ShiftHandover> findUnacknowledgedHandoversOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);
}