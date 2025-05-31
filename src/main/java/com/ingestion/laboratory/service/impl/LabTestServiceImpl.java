package com.ingestion.laboratory.service.impl;

import com.ingestion.laboratory.model.LabTest;
import com.ingestion.laboratory.model.TestParameter;
import com.ingestion.laboratory.repository.LabTestRepository;
import com.ingestion.laboratory.repository.TestParameterRepository;
import com.ingestion.laboratory.service.LabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LabTestServiceImpl implements LabTestService {

    @Autowired
    private LabTestRepository labTestRepository;
    
    @Autowired
    private TestParameterRepository testParameterRepository;

    @Override
    @Transactional
    public LabTest saveTest(LabTest labTest) {
        return labTestRepository.save(labTest);
    }

    @Override
    public Optional<LabTest> findById(Long id) {
        return labTestRepository.findById(id);
    }

    @Override
    public Optional<LabTest> findByTestCode(String testCode) {
        return labTestRepository.findByTestCode(testCode);
    }

    @Override
    public List<LabTest> findAll() {
        return labTestRepository.findAll();
    }

    @Override
    public List<LabTest> findByTestName(String testName) {
        return labTestRepository.findByTestNameContainingIgnoreCase(testName);
    }

    @Override
    public List<LabTest> findByCategory(String category) {
        return labTestRepository.findByCategory(category);
    }

    @Override
    public List<LabTest> findByDepartment(String department) {
        return labTestRepository.findByDepartment(department);
    }

    @Override
    public List<LabTest> findBySampleType(String sampleType) {
        return labTestRepository.findBySampleType(sampleType);
    }

    @Override
    public List<LabTest> findAllActiveTests() {
        return labTestRepository.findAllActiveTests();
    }

    @Override
    public List<LabTest> findActiveByCategoryOrderByName(String category) {
        return labTestRepository.findActiveByCategoryOrderByName(category);
    }

    @Override
    public List<String> findAllActiveCategories() {
        return labTestRepository.findAllActiveCategories();
    }

    @Override
    public List<String> findAllActiveDepartments() {
        return labTestRepository.findAllActiveDepartments();
    }

    @Override
    public List<String> findAllActiveSampleTypes() {
        return labTestRepository.findAllActiveSampleTypes();
    }

    @Override
    @Transactional
    public LabTest createTest(LabTest labTest, List<TestParameter> parameters) {
        LabTest savedTest = labTestRepository.save(labTest);
        
        if (parameters != null && !parameters.isEmpty()) {
            for (TestParameter parameter : parameters) {
                parameter.setLabTest(savedTest);
                testParameterRepository.save(parameter);
            }
        }
        
        return savedTest;
    }

    @Override
    @Transactional
    public LabTest updateTest(Long id, LabTest labTest) {
        Optional<LabTest> existingTestOpt = labTestRepository.findById(id);
        if (existingTestOpt.isPresent()) {
            LabTest existingTest = existingTestOpt.get();
            
            // Update fields
            existingTest.setTestName(labTest.getTestName());
            existingTest.setDescription(labTest.getDescription());
            existingTest.setCategory(labTest.getCategory());
            existingTest.setDepartment(labTest.getDepartment());
            existingTest.setPrice(labTest.getPrice());
            existingTest.setSampleType(labTest.getSampleType());
            existingTest.setSampleVolume(labTest.getSampleVolume());
            existingTest.setContainerType(labTest.getContainerType());
            existingTest.setProcessingTime(labTest.getProcessingTime());
            existingTest.setFastingRequired(labTest.getFastingRequired());
            existingTest.setSpecialInstructions(labTest.getSpecialInstructions());
            existingTest.setIsActive(labTest.getIsActive());
            
            return labTestRepository.save(existingTest);
        } else {
            throw new RuntimeException("Test not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public LabTest addParameter(Long testId, TestParameter parameter) {
        Optional<LabTest> testOpt = labTestRepository.findById(testId);
        if (testOpt.isPresent()) {
            LabTest test = testOpt.get();
            parameter.setLabTest(test);
            testParameterRepository.save(parameter);
            return test;
        } else {
            throw new RuntimeException("Test not found with id: " + testId);
        }
    }

    @Override
    @Transactional
    public LabTest updateParameter(Long testId, Long parameterId, TestParameter parameter) {
        Optional<LabTest> testOpt = labTestRepository.findById(testId);
        if (testOpt.isPresent()) {
            LabTest test = testOpt.get();
            
            Optional<TestParameter> existingParamOpt = testParameterRepository.findById(parameterId);
            if (existingParamOpt.isPresent()) {
                TestParameter existingParam = existingParamOpt.get();
                
                // Ensure the parameter belongs to this test
                if (!existingParam.getLabTest().getId().equals(testId)) {
                    throw new RuntimeException("Parameter does not belong to the specified test");
                }
                
                // Update fields
                existingParam.setParameterName(parameter.getParameterName());
                existingParam.setUnit(parameter.getUnit());
                existingParam.setReferenceRange(parameter.getReferenceRange());
                existingParam.setMaleReferenceRange(parameter.getMaleReferenceRange());
                existingParam.setFemaleReferenceRange(parameter.getFemaleReferenceRange());
                existingParam.setChildReferenceRange(parameter.getChildReferenceRange());
                existingParam.setCriticalLow(parameter.getCriticalLow());
                existingParam.setCriticalHigh(parameter.getCriticalHigh());
                existingParam.setDisplayOrder(parameter.getDisplayOrder());
                existingParam.setIsActive(parameter.getIsActive());
                
                testParameterRepository.save(existingParam);
                return test;
            } else {
                throw new RuntimeException("Parameter not found with id: " + parameterId);
            }
        } else {
            throw new RuntimeException("Test not found with id: " + testId);
        }
    }

    @Override
    @Transactional
    public LabTest removeParameter(Long testId, Long parameterId) {
        Optional<LabTest> testOpt = labTestRepository.findById(testId);
        if (testOpt.isPresent()) {
            LabTest test = testOpt.get();
            
            Optional<TestParameter> paramOpt = testParameterRepository.findById(parameterId);
            if (paramOpt.isPresent()) {
                TestParameter parameter = paramOpt.get();
                
                // Ensure the parameter belongs to this test
                if (!parameter.getLabTest().getId().equals(testId)) {
                    throw new RuntimeException("Parameter does not belong to the specified test");
                }
                
                test.removeParameter(parameter);
                testParameterRepository.delete(parameter);
                return test;
            } else {
                throw new RuntimeException("Parameter not found with id: " + parameterId);
            }
        } else {
            throw new RuntimeException("Test not found with id: " + testId);
        }
    }

    @Override
    @Transactional
    public LabTest activateTest(Long id) {
        Optional<LabTest> testOpt = labTestRepository.findById(id);
        if (testOpt.isPresent()) {
            LabTest test = testOpt.get();
            test.setIsActive(true);
            return labTestRepository.save(test);
        } else {
            throw new RuntimeException("Test not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public LabTest deactivateTest(Long id) {
        Optional<LabTest> testOpt = labTestRepository.findById(id);
        if (testOpt.isPresent()) {
            LabTest test = testOpt.get();
            test.setIsActive(false);
            return labTestRepository.save(test);
        } else {
            throw new RuntimeException("Test not found with id: " + id);
        }
    }

    @Override
    @Transactional
    public void deleteTest(Long id) {
        labTestRepository.deleteById(id);
    }
}