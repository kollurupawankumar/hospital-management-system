package com.ingestion.billing.service;

import com.ingestion.billing.model.AccountingEntry;
import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountingEntryService {

    AccountingEntry saveAccountingEntry(AccountingEntry accountingEntry);
    
    Optional<AccountingEntry> findById(Long id);
    
    Optional<AccountingEntry> findByEntryNumber(String entryNumber);
    
    List<AccountingEntry> findAll();
    
    List<AccountingEntry> findByEntryType(AccountingEntry.EntryType entryType);
    
    List<AccountingEntry> findByReferenceType(AccountingEntry.ReferenceType referenceType);
    
    List<AccountingEntry> findByReferenceId(Long referenceId);
    
    List<AccountingEntry> findByReferenceNumber(String referenceNumber);
    
    List<AccountingEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<AccountingEntry> findByIsReconciled(Boolean isReconciled);
    
    List<AccountingEntry> findByEntryTypeOrderByEntryDateDesc(AccountingEntry.EntryType entryType);
    
    List<AccountingEntry> findByReferenceTypeAndReferenceId(
            AccountingEntry.ReferenceType referenceType, Long referenceId);
    
    List<AccountingEntry> findByReferenceTypeAndReferenceNumber(
            AccountingEntry.ReferenceType referenceType, String referenceNumber);
    
    Page<AccountingEntry> findRecentEntries(LocalDate startDate, Pageable pageable);
    
    Double getTotalAmountByEntryTypeForPeriod(
            AccountingEntry.EntryType entryType, LocalDate startDate, LocalDate endDate);
    
    List<AccountingEntry> findByAccountCode(String accountCode);
    
    List<AccountingEntry> findByDebitOrCreditAccountCode(String accountCode);
    
    Long countByEntryType(AccountingEntry.EntryType entryType);
    
    Long countByReconciliationStatus(Boolean isReconciled);
    
    Long countByDateRange(LocalDate startDate, LocalDate endDate);
    
    AccountingEntry createAccountingEntry(LocalDate entryDate, AccountingEntry.EntryType entryType, 
                                         BigDecimal amount, String description, 
                                         AccountingEntry.ReferenceType referenceType, Long referenceId, 
                                         String referenceNumber, String accountCode, String accountName, 
                                         String debitAccountCode, String debitAccountName, 
                                         String creditAccountCode, String creditAccountName, 
                                         String notes, User createdBy);
    
    AccountingEntry createEntryFromInvoice(Invoice invoice, User createdBy);
    
    AccountingEntry createEntryFromPayment(Payment payment, User createdBy);
    
    AccountingEntry reconcileEntry(Long id, User reconciledBy);
    
    void deleteAccountingEntry(Long id);
}