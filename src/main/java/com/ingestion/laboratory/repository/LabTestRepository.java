package com.ingestion.laboratory.repository;

import com.ingestion.laboratory.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {

    Optional<LabTest> findByTestCode(String testCode);
    
    List<LabTest> findByTestNameContainingIgnoreCase(String testName);
    
    List<LabTest> findByCategory(String category);
    
    List<LabTest> findByDepartment(String department);
    
    List<LabTest> findBySampleType(String sampleType);
    
    List<LabTest> findByIsActive(Boolean isActive);
    
    @Query("SELECT t FROM LabTest t WHERE t.isActive = true ORDER BY t.testName")
    List<LabTest> findAllActiveTests();
    
    @Query("SELECT t FROM LabTest t WHERE t.category = :category AND t.isActive = true ORDER BY t.testName")
    List<LabTest> findActiveByCategoryOrderByName(@Param("category") String category);
    
    @Query("SELECT DISTINCT t.category FROM LabTest t WHERE t.isActive = true ORDER BY t.category")
    List<String> findAllActiveCategories();
    
    @Query("SELECT DISTINCT t.department FROM LabTest t WHERE t.isActive = true ORDER BY t.department")
    List<String> findAllActiveDepartments();
    
    @Query("SELECT DISTINCT t.sampleType FROM LabTest t WHERE t.isActive = true ORDER BY t.sampleType")
    List<String> findAllActiveSampleTypes();
}