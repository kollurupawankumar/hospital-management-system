package com.ingestion.laboratory.repository;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.laboratory.model.LabOrderItem;
import com.ingestion.laboratory.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderItemRepository extends JpaRepository<LabOrderItem, Long> {

    List<LabOrderItem> findByLabOrder(LabOrder labOrder);
    
    List<LabOrderItem> findByLabTest(LabTest labTest);
    
    List<LabOrderItem> findByStatus(LabOrderItem.ItemStatus status);
    
    List<LabOrderItem> findByLabOrderAndStatus(LabOrder labOrder, LabOrderItem.ItemStatus status);
    
    @Query("SELECT i FROM LabOrderItem i WHERE i.labOrder = :labOrder ORDER BY i.labTest.testName")
    List<LabOrderItem> findByLabOrderOrderByTestName(@Param("labOrder") LabOrder labOrder);
    
    @Query("SELECT COUNT(i) FROM LabOrderItem i WHERE i.labOrder = :labOrder AND i.status = :status")
    Long countByLabOrderAndStatus(
            @Param("labOrder") LabOrder labOrder,
            @Param("status") LabOrderItem.ItemStatus status);
    
    @Query("SELECT COUNT(i) FROM LabOrderItem i WHERE i.labTest = :labTest AND i.status = :status")
    Long countByLabTestAndStatus(
            @Param("labTest") LabTest labTest,
            @Param("status") LabOrderItem.ItemStatus status);
}