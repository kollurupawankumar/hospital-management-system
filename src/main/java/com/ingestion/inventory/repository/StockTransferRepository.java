package com.ingestion.inventory.repository;

import com.ingestion.inventory.model.StockTransfer;
import com.ingestion.inventory.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    
    Optional<StockTransfer> findByTransferNumber(String transferNumber);
    
    List<StockTransfer> findBySourceStore(Store sourceStore);
    
    List<StockTransfer> findByDestinationStore(Store destinationStore);
    
    List<StockTransfer> findByStatus(StockTransfer.TransferStatus status);
    
    List<StockTransfer> findByTransferDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<StockTransfer> findByIsCancelled(Boolean isCancelled);
    
    List<StockTransfer> findBySourceStoreAndDestinationStore(Store sourceStore, Store destinationStore);
}