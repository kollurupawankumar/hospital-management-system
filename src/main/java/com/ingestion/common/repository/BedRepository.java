package com.ingestion.common.repository;

import com.ingestion.common.model.inpatient.Bed;
import com.ingestion.common.model.inpatient.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    
    List<Bed> findByWard(Ward ward);
    
    List<Bed> findByWardAndStatus(Ward ward, Bed.BedStatus status);
    
    List<Bed> findByStatus(Bed.BedStatus status);
    
    List<Bed> findByWardAndIsActiveTrue(Ward ward);
    
    Optional<Bed> findByBedNumber(String bedNumber);
    
    @Query("SELECT b FROM Bed b WHERE b.ward = ?1 AND b.status = 'AVAILABLE' AND b.isActive = true")
    List<Bed> findAvailableBedsByWard(Ward ward);
    
    @Query("SELECT b FROM Bed b WHERE b.status = 'AVAILABLE' AND b.isActive = true")
    List<Bed> findAllAvailableBeds();
    
    @Query("SELECT b FROM Bed b WHERE b.ward.department.id = ?1 AND b.status = 'AVAILABLE' AND b.isActive = true")
    List<Bed> findAvailableBedsByDepartmentId(Long departmentId);
    
    @Query("SELECT b FROM Bed b WHERE b.bedType = ?1 AND b.status = 'AVAILABLE' AND b.isActive = true")
    List<Bed> findAvailableBedsByType(Bed.BedType bedType);
}