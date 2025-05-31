package com.ingestion.laboratory.service;

import com.ingestion.laboratory.model.LabTest;
import com.ingestion.laboratory.model.TestParameter;

import java.util.List;
import java.util.Optional;

public interface LabTestService {

    LabTest saveTest(LabTest labTest);
    
    Optional<LabTest> findById(Long id);
    
    Optional<LabTest> findByTestCode(String testCode);
    
    List<LabTest> findAll();
    
    List<LabTest> findByTestName(String testName);
    
    List<LabTest> findByCategory(String category);
    
    List<LabTest> findByDepartment(String department);
    
    List<LabTest> findBySampleType(String sampleType);
    
    List<LabTest> findAllActiveTests();
    
    List<LabTest> findActiveByCategoryOrderByName(String category);
    
    List<String> findAllActiveCategories();
    
    List<String> findAllActiveDepartments();
    
    List<String> findAllActiveSampleTypes();
    
    LabTest createTest(LabTest labTest, List<TestParameter> parameters);
    
    LabTest updateTest(Long id, LabTest labTest);
    
    LabTest addParameter(Long testId, TestParameter parameter);
    
    LabTest updateParameter(Long testId, Long parameterId, TestParameter parameter);
    
    LabTest removeParameter(Long testId, Long parameterId);
    
    LabTest activateTest(Long id);
    
    LabTest deactivateTest(Long id);
    
    void deleteTest(Long id);
}