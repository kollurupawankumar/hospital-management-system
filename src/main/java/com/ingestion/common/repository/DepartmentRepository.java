package com.ingestion.common.repository;

import com.ingestion.common.model.inpatient.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    Optional<Department> findByName(String name);
    
    List<Department> findByIsActiveTrue();
    
    List<Department> findByLocation(String location);
}