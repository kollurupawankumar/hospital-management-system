package com.ingestion.billing.service.impl;

import com.ingestion.billing.model.AccountingEntry;
import com.ingestion.billing.repository.AccountingEntryRepository;
import com.ingestion.billing.service.AccountingEntryService;
import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountingEntryServiceImpl implements AccountingEntryService {

    @Autowired
    private AccountingEntryRepository accountingEntryRepository;

    @Override
    public AccountingEntry saveAccountingEntry(AccountingEntry accountingEntry) {
        return accountingEntryRepository.save(accountingEntry);
    }

    @Override
    public Optional<AccountingEntry> findById(Long id) {
        return accountingEntryRepository.findById(id);
    }

    @Override
    public Optional<AccountingEntry> findByEntryNumber(String entryNumber) {
        return accountingEntryRepository.findByEntryNumber(entryNumber);
    }

    @Override
    public List<AccountingEntry> findAll() {
        return accountingEntryRepository.findAll();
    }

    @Override
    public List<AccountingEntry> findByEntryType(AccountingEntry.EntryType entryType) {
        return accountingEntryRepository.findByEntryType(entryType);
    }

    @Override
    public List<AccountingEntry> findByReferenceType(AccountingEntry.ReferenceType referenceType) {
        return accountingEntryRepository.findByReferenceType(referenceType);
    }

    @Override
    public List<AccountingEntry> findByReferenceId(Long referenceId) {
        return accountingEntryRepository.findByReferenceId(referenceId);
    }

    @Override
    public List<AccountingEntry> findByReferenceNumber(String referenceNumber) {
        return accountingEntryRepository.findByReferenceNumber(referenceNumber);
    }

    @Override
    public List<AccountingEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate) {
        return accountingEntryRepository.findByEntryDateBetween(startDate, endDate);
    }

    @Override
    public List<AccountingEntry> findByIsReconciled(Boolean isReconciled) {
        return accountingEntryRepository.findByIsReconciled(isReconciled);
    }

    @Override
    public List<AccountingEntry> findByEntryTypeOrderByEntryDateDesc(AccountingEntry.EntryType entryType) {
        return accountingEntryRepository.findByEntryTypeOrderByEntryDateDesc(entryType);
    }

    @Override
    public List<AccountingEntry> findByReferenceTypeAndReferenceId(
            AccountingEntry.ReferenceType referenceType, Long referenceId) {
        return accountingEntryRepository.findByReferenceTypeAndReferenceId(referenceType, referenceId);
    }

    @Override
    public List<AccountingEntry> findByReferenceTypeAndReferenceNumber(
            AccountingEntry.ReferenceType referenceType, String referenceNumber) {
        return accountingEntryRepository.findByReferenceTypeAndReferenceNumber(referenceType, referenceNumber);
    }

    @Override
    public Page<AccountingEntry> findRecentEntries(LocalDate startDate, Pageable pageable) {
        return accountingEntryRepository.findRecentEntries(startDate, pageable);
    }

    @Override
    public Double getTotalAmountByEntryTypeForPeriod(
            AccountingEntry.EntryType entryType, LocalDate startDate, LocalDate endDate) {
        return accountingEntryRepository.getTotalAmountByEntryTypeForPeriod(entryType, startDate, endDate);
    }

    @Override
    public List<AccountingEntry> findByAccountCode(String accountCode) {
        return accountingEntryRepository.findByAccountCode(accountCode);
    }

    @Override
    public List<AccountingEntry> findByDebitOrCreditAccountCode(String accountCode) {
        return accountingEntryRepository.findByDebitOrCreditAccountCode(accountCode);
    }

    @Override
    public Long countByEntryType(AccountingEntry.EntryType entryType) {
        return accountingEntryRepository.countByEntryType(entryType);
    }

    @Override
    public Long countByReconciliationStatus(Boolean isReconciled) {
        return accountingEntryRepository.countByReconciliationStatus(isReconciled);
    }

    @Override
    public Long countByDateRange(LocalDate startDate, LocalDate endDate) {
        return accountingEntryRepository.countByDateRange(startDate, endDate);
    }

    @Override
    public AccountingEntry createAccountingEntry(LocalDate entryDate, AccountingEntry.EntryType entryType, 
                                               BigDecimal amount, String description, 
                                               AccountingEntry.ReferenceType referenceType, Long referenceId, 
                                               String referenceNumber, String accountCode, String accountName, 
                                               String debitAccountCode, String debitAccountName, 
                                               String creditAccountCode, String creditAccountName, 
                                               String notes, User createdBy) {
        AccountingEntry entry = new AccountingEntry();
        entry.setEntryDate(entryDate);
        entry.setEntryType(entryType);
        entry.setAmount(amount);
        entry.setDescription(description);
        entry.setReferenceType(referenceType);
        entry.setReferenceId(referenceId);
        entry.setReferenceNumber(referenceNumber);
        entry.setAccountCode(accountCode);
        entry.setAccountName(accountName);
        entry.setDebitAccountCode(debitAccountCode);
        entry.setDebitAccountName(debitAccountName);
        entry.setCreditAccountCode(creditAccountCode);
        entry.setCreditAccountName(creditAccountName);
        entry.setNotes(notes);
        entry.setCreator(createdBy);
        entry.setCreatedDate(LocalDateTime.now());
        
        return saveAccountingEntry(entry);
    }

    @Override
    public AccountingEntry createEntryFromInvoice(Invoice invoice, User createdBy) {
        return createAccountingEntry(
            invoice.getInvoiceDate().toLocalDate(),
            AccountingEntry.EntryType.INVOICE,
            invoice.getTotalAmount(),
            "Invoice: " + invoice.getInvoiceNumber(),
            AccountingEntry.ReferenceType.INVOICE,
            invoice.getId(),
            invoice.getInvoiceNumber(),
            "4000", // Revenue account
            "Patient Revenue",
            "1200", // Accounts Receivable
            "Accounts Receivable",
            "4000", // Revenue account
            "Patient Revenue",
            "Accounting entry for invoice " + invoice.getInvoiceNumber(),
            createdBy
        );
    }

    @Override
    public AccountingEntry createEntryFromPayment(Payment payment, User createdBy) {
        String accountCode = getAccountCodeForPaymentMethod(payment.getPaymentMethod());
        String accountName = getAccountNameForPaymentMethod(payment.getPaymentMethod());
        
        return createAccountingEntry(
            payment.getPaymentDate().toLocalDate(),
            AccountingEntry.EntryType.PAYMENT,
            payment.getAmount(),
            "Payment: " + payment.getPaymentNumber(),
            AccountingEntry.ReferenceType.PAYMENT,
            payment.getId(),
            payment.getPaymentNumber(),
            accountCode,
            accountName,
            accountCode,
            accountName,
            "1200", // Accounts Receivable
            "Accounts Receivable",
            "Accounting entry for payment " + payment.getPaymentNumber(),
            createdBy
        );
    }

    @Override
    public AccountingEntry reconcileEntry(Long id, User reconciledBy) {
        Optional<AccountingEntry> entryOpt = findById(id);
        if (entryOpt.isPresent()) {
            AccountingEntry entry = entryOpt.get();
            entry.setIsReconciled(true);
            entry.setReconciledBy(reconciledBy);
            entry.setReconciliationDate(LocalDateTime.now());
            
            return saveAccountingEntry(entry);
        } else {
            throw new IllegalArgumentException("Accounting entry not found with id: " + id);
        }
    }

    @Override
    public void deleteAccountingEntry(Long id) {
        accountingEntryRepository.deleteById(id);
    }

    private String getAccountCodeForPaymentMethod(Payment.PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case CASH:
                return "1000";
            case CREDIT_CARD:
            case DEBIT_CARD:
            case CARD:
                return "1100";
            case BANK_TRANSFER:
                return "1110";
            case CHEQUE:
                return "1120";
            case UPI:
                return "1130";
            case MOBILE_WALLET:
            case WALLET:
                return "1140";
            case ONLINE:
                return "1150";
            case INSURANCE:
                return "1160";
            default:
                return "1000";
        }
    }

    private String getAccountNameForPaymentMethod(Payment.PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case CASH:
                return "Cash";
            case CREDIT_CARD:
            case DEBIT_CARD:
            case CARD:
                return "Card Payments";
            case BANK_TRANSFER:
                return "Bank Transfer";
            case CHEQUE:
                return "Cheque Payments";
            case UPI:
                return "UPI Payments";
            case MOBILE_WALLET:
            case WALLET:
                return "Wallet Payments";
            case ONLINE:
                return "Online Payments";
            case INSURANCE:
                return "Insurance Payments";
            default:
                return "Cash";
        }
    }
}