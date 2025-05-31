package com.ingestion.billing.repository;

import com.ingestion.billing.model.AccountingEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountingEntryRepository extends JpaRepository<AccountingEntry, Long> {

    Optional<AccountingEntry> findByEntryNumber(String entryNumber);
    
    List<AccountingEntry> findByEntryType(AccountingEntry.EntryType entryType);
    
    List<AccountingEntry> findByReferenceType(AccountingEntry.ReferenceType referenceType);
    
    List<AccountingEntry> findByReferenceId(Long referenceId);
    
    List<AccountingEntry> findByReferenceNumber(String referenceNumber);
    
    List<AccountingEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<AccountingEntry> findByIsReconciled(Boolean isReconciled);
    
    @Query("SELECT ae FROM AccountingEntry ae WHERE ae.entryType = :entryType ORDER BY ae.entryDate DESC")
    List<AccountingEntry> findByEntryTypeOrderByEntryDateDesc(@Param("entryType") AccountingEntry.EntryType entryType);
    
    @Query("SELECT ae FROM AccountingEntry ae WHERE ae.referenceType = :referenceType AND ae.referenceId = :referenceId ORDER BY ae.entryDate DESC")
    List<AccountingEntry> findByReferenceTypeAndReferenceId(
            @Param("referenceType") AccountingEntry.ReferenceType referenceType, 
            @Param("referenceId") Long referenceId);
    
    @Query("SELECT ae FROM AccountingEntry ae WHERE ae.referenceType = :referenceType AND ae.referenceNumber = :referenceNumber ORDER BY ae.entryDate DESC")
    List<AccountingEntry> findByReferenceTypeAndReferenceNumber(
            @Param("referenceType") AccountingEntry.ReferenceType referenceType, 
            @Param("referenceNumber") String referenceNumber);
    
    @Query("SELECT ae FROM AccountingEntry ae WHERE ae.entryDate >= :startDate ORDER BY ae.entryDate DESC")
    Page<AccountingEntry> findRecentEntries(@Param("startDate") LocalDate startDate, Pageable pageable);
    
    @Query("SELECT SUM(ae.amount) FROM AccountingEntry ae WHERE ae.entryType = :entryType AND ae.entryDate BETWEEN :startDate AND :endDate")
    Double getTotalAmountByEntryTypeForPeriod(
            @Param("entryType") AccountingEntry.EntryType entryType, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ae FROM AccountingEntry ae WHERE ae.accountCode = :accountCode ORDER BY ae.entryDate DESC")
    List<AccountingEntry> findByAccountCode(@Param("accountCode") String accountCode);
    
    @Query("SELECT ae FROM AccountingEntry ae WHERE ae.debitAccountCode = :accountCode OR ae.creditAccountCode = :accountCode ORDER BY ae.entryDate DESC")
    List<AccountingEntry> findByDebitOrCreditAccountCode(@Param("accountCode") String accountCode);
    
    @Query("SELECT COUNT(ae) FROM AccountingEntry ae WHERE ae.entryType = :entryType")
    Long countByEntryType(@Param("entryType") AccountingEntry.EntryType entryType);
    
    @Query("SELECT COUNT(ae) FROM AccountingEntry ae WHERE ae.isReconciled = :isReconciled")
    Long countByReconciliationStatus(@Param("isReconciled") Boolean isReconciled);
    
    @Query("SELECT COUNT(ae) FROM AccountingEntry ae WHERE ae.entryDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}