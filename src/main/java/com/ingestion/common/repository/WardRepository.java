package com.ingestion.common.repository;

import com.ingestion.common.model.inpatient.Department;
import com.ingestion.common.model.inpatient.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    
    List<Ward> findByDepartment(Department department);
    
    List<Ward> findByDepartmentAndIsActiveTrue(Department department);
    
    List<Ward> findByWardType(Ward.WardType wardType);
    
    List<Ward> findByIsActiveTrue();
    
    @Query("SELECT w FROM Ward w WHERE w.availableBeds > 0 AND w.isActive = true")
    List<Ward> findAllWithAvailableBeds();
    
    @Query("SELECT w FROM Ward w WHERE w.department = ?1 AND w.availableBeds > 0 AND w.isActive = true")
    List<Ward> findByDepartmentWithAvailableBeds(Department department);
    
    @Query("SELECT w FROM Ward w WHERE w.wardType = ?1 AND w.availableBeds > 0 AND w.isActive = true")
    List<Ward> findByWardTypeWithAvailableBeds(Ward.WardType wardType);
}