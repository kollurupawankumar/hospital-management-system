package com.ingestion.common.laboratory;

import com.ingestion.laboratory.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for LabOrderTest entities.
 */
@Repository
public interface LabOrderTestRepository extends JpaRepository<LabOrderTest, Long> {
    
    List<LabOrderTest> findByLabOrder(LabOrder labOrder);
    
    List<LabOrderTest> findByLabOrderId(Long labOrderId);
    
    List<LabOrderTest> findByLabTest(LabTest labTest);
    
    List<LabOrderTest> findByLabTestId(Long labTestId);
    
    @Query("SELECT t FROM LabOrderTest t WHERE t.labOrder.id = :orderId AND t.labTest.id = :testId")
    LabOrderTest findByLabOrderIdAndLabTestId(@Param("orderId") Long orderId, @Param("testId") Long testId);
    
    @Query("SELECT t FROM LabOrderTest t WHERE t.isAbnormal = true")
    List<LabOrderTest> findAbnormalResults();
    
    @Query("SELECT t FROM LabOrderTest t WHERE t.isCritical = true")
    List<LabOrderTest> findCriticalResults();
    
    @Query("SELECT COUNT(t) FROM LabOrderTest t WHERE t.labTest.id = :testId")
    Long countByLabTestId(@Param("testId") Long testId);
}