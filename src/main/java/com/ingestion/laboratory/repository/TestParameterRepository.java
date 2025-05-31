package com.ingestion.laboratory.repository;

import com.ingestion.laboratory.model.LabTest;
import com.ingestion.laboratory.model.TestParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestParameterRepository extends JpaRepository<TestParameter, Long> {

    List<TestParameter> findByLabTest(LabTest labTest);
    
    List<TestParameter> findByLabTestAndIsActive(LabTest labTest, Boolean isActive);
    
    List<TestParameter> findByParameterNameContainingIgnoreCase(String parameterName);
    
    @Query("SELECT p FROM TestParameter p WHERE p.labTest = :labTest ORDER BY p.displayOrder")
    List<TestParameter> findByLabTestOrderByDisplayOrder(@Param("labTest") LabTest labTest);
    
    @Query("SELECT p FROM TestParameter p WHERE p.labTest = :labTest AND p.isActive = true ORDER BY p.displayOrder")
    List<TestParameter> findActiveByLabTestOrderByDisplayOrder(@Param("labTest") LabTest labTest);
}