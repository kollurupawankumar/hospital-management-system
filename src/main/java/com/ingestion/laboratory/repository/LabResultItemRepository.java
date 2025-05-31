package com.ingestion.laboratory.repository;

import com.ingestion.laboratory.model.LabResult;
import com.ingestion.laboratory.model.LabResultItem;
import com.ingestion.laboratory.model.TestParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabResultItemRepository extends JpaRepository<LabResultItem, Long> {

    List<LabResultItem> findByLabResult(LabResult labResult);
    
    List<LabResultItem> findByTestParameter(TestParameter testParameter);
    
    Optional<LabResultItem> findByLabResultAndTestParameter(LabResult labResult, TestParameter testParameter);
    
    List<LabResultItem> findByIsAbnormal(Boolean isAbnormal);
    
    List<LabResultItem> findByIsCritical(Boolean isCritical);
    
    List<LabResultItem> findByLabResultAndIsAbnormal(LabResult labResult, Boolean isAbnormal);
    
    List<LabResultItem> findByLabResultAndIsCritical(LabResult labResult, Boolean isCritical);
    
    @Query("SELECT i FROM LabResultItem i WHERE i.labResult = :labResult ORDER BY i.testParameter.displayOrder")
    List<LabResultItem> findByLabResultOrderByParameterOrder(@Param("labResult") LabResult labResult);
    
    @Query("SELECT COUNT(i) FROM LabResultItem i WHERE i.labResult = :labResult AND i.isAbnormal = true")
    Long countAbnormalByLabResult(@Param("labResult") LabResult labResult);
    
    @Query("SELECT COUNT(i) FROM LabResultItem i WHERE i.labResult = :labResult AND i.isCritical = true")
    Long countCriticalByLabResult(@Param("labResult") LabResult labResult);
}