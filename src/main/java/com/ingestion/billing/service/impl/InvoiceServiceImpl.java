package com.ingestion.billing.service.impl;

import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.InvoiceItem;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.billing.repository.InvoiceRepository;
import com.ingestion.billing.service.AccountingEntryService;
import com.ingestion.billing.service.InvoiceService;
import com.ingestion.billing.service.PaymentService;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private AccountingEntryService accountingEntryService;

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public List<Invoice> findByPatient(Patient patient) {
        return invoiceRepository.findByPatient(patient);
    }

    @Override
    public List<Invoice> findByInvoiceType(Invoice.InvoiceType invoiceType) {
        return invoiceRepository.findByInvoiceType(invoiceType);
    }

    @Override
    public List<Invoice> findByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    @Override
    public List<Invoice> findByPaymentStatus(Invoice.PaymentStatus paymentStatus) {
        return invoiceRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public List<Invoice> findByInvoiceDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return invoiceRepository.findByInvoiceDateBetween(startDate, endDate);
    }

    @Override
    public List<Invoice> findByDueDateBefore(LocalDateTime date) {
        return invoiceRepository.findByDueDateBefore(date);
    }

    @Override
    public List<Invoice> findByIsCancelled(Boolean isCancelled) {
        return invoiceRepository.findByIsCancelled(isCancelled);
    }

    @Override
    public List<Invoice> findByPatientOrderByInvoiceDateDesc(Long patientId) {
        return invoiceRepository.findByPatientOrderByInvoiceDateDesc(patientId);
    }

    @Override
    public List<Invoice> findByStatusOrderByInvoiceDateDesc(Invoice.InvoiceStatus status) {
        return invoiceRepository.findByStatusOrderByInvoiceDateDesc(status);
    }

    @Override
    public List<Invoice> findByPaymentStatusOrderByInvoiceDateDesc(Invoice.PaymentStatus paymentStatus) {
        return invoiceRepository.findByPaymentStatusOrderByInvoiceDateDesc(paymentStatus);
    }

    @Override
    public Page<Invoice> findRecentInvoices(LocalDateTime startDate, Pageable pageable) {
        return invoiceRepository.findRecentInvoices(startDate, pageable);
    }

    @Override
    public List<Invoice> findOverdueInvoices() {
        return invoiceRepository.findOverdueInvoices();
    }

    @Override
    public Double getTotalInvoiceAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return invoiceRepository.getTotalInvoiceAmountForPeriod(startDate, endDate);
    }

    @Override
    public Double getTotalPaidAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return invoiceRepository.getTotalPaidAmountForPeriod(startDate, endDate);
    }

    @Override
    public Long countByStatus(Invoice.InvoiceStatus status) {
        return invoiceRepository.countByStatus(status);
    }

    @Override
    public Long countByPaymentStatus(Invoice.PaymentStatus paymentStatus) {
        return invoiceRepository.countByPaymentStatus(paymentStatus);
    }

    @Override
    public Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return invoiceRepository.countByDateRange(startDate, endDate);
    }

    @Override
    @Transactional
    public Invoice createInvoice(Patient patient, Invoice.InvoiceType invoiceType, LocalDateTime dueDate, 
                               String notes, User createdBy, List<InvoiceItem> items) {
        Invoice invoice = new Invoice();
        invoice.setPatient(patient);
        invoice.setInvoiceType(invoiceType);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setDueDate(dueDate);
        invoice.setNotes(notes);
        invoice.setCreator(createdBy);
        invoice.setStatus(Invoice.InvoiceStatus.DRAFT);
        
        if (items != null && !items.isEmpty()) {
            for (InvoiceItem item : items) {
                invoice.addInvoiceItem(item);
            }
        }
        
        invoice.calculateTotals();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        
        // Create accounting entry for the invoice
        accountingEntryService.createEntryFromInvoice(savedInvoice, createdBy);
        
        return savedInvoice;
    }

    @Override
    @Transactional
    public Invoice addItemToInvoice(Long invoiceId, InvoiceItem.ServiceType serviceType, Long serviceId, 
                                  String serviceCode, String description, Integer quantity, BigDecimal unitPrice, 
                                  BigDecimal discountPercentage, BigDecimal taxPercentage) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        if (invoice.getStatus() != Invoice.InvoiceStatus.DRAFT) {
            throw new RuntimeException("Cannot add items to an invoice that is not in DRAFT status");
        }
        
        InvoiceItem item = new InvoiceItem();
        item.setServiceType(serviceType);
        item.setServiceId(serviceId);
        item.setServiceCode(serviceCode);
        item.setDescription(description);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        
        if (discountPercentage != null) {
            item.setDiscountPercentage(discountPercentage);
        }
        
        if (taxPercentage != null) {
            item.setTaxPercentage(taxPercentage);
        }
        
        invoice.addInvoiceItem(item);
        invoice.calculateTotals();
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice removeItemFromInvoice(Long invoiceId, Long itemId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        if (invoice.getStatus() != Invoice.InvoiceStatus.DRAFT) {
            throw new RuntimeException("Cannot remove items from an invoice that is not in DRAFT status");
        }
        
        InvoiceItem itemToRemove = null;
        for (InvoiceItem item : invoice.getInvoiceItems()) {
            if (item.getId().equals(itemId)) {
                itemToRemove = item;
                break;
            }
        }
        
        if (itemToRemove != null) {
            invoice.removeInvoiceItem(itemToRemove);
            invoice.calculateTotals();
            return invoiceRepository.save(invoice);
        } else {
            throw new RuntimeException("Invoice item not found with id: " + itemId);
        }
    }

    @Override
    @Transactional
    public Invoice applyDiscount(Long invoiceId, BigDecimal discountAmount) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        invoice.setDiscountAmount(discountAmount);
        invoice.calculateTotals();
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice applyTax(Long invoiceId, BigDecimal taxAmount) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        invoice.setTaxAmount(taxAmount);
        invoice.calculateTotals();
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice calculateTotals(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        invoice.calculateTotals();
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice issue(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        if (invoice.getStatus() != Invoice.InvoiceStatus.DRAFT) {
            throw new RuntimeException("Only invoices in DRAFT status can be issued");
        }
        
        invoice.issue();
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice markAsOverdue(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        if (invoice.getStatus() != Invoice.InvoiceStatus.ISSUED) {
            throw new RuntimeException("Only issued invoices can be marked as overdue");
        }
        
        if (!invoice.isOverdue()) {
            throw new RuntimeException("Invoice is not overdue yet");
        }
        
        invoice.markAsOverdue();
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice cancel(Long invoiceId, User cancelledBy, String reason) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        if (invoice.getIsCancelled()) {
            throw new RuntimeException("Invoice is already cancelled");
        }
        
        invoice.cancel(cancelledBy, reason);
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice recordPayment(Long invoiceId, BigDecimal amount, Payment.PaymentMethod paymentMethod, 
                               String paymentReference, String notes, User receivedBy) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        if (invoice.getIsCancelled()) {
            throw new RuntimeException("Cannot record payment for a cancelled invoice");
        }
        
        if (amount.compareTo(invoice.getBalanceAmount()) > 0) {
            throw new RuntimeException("Payment amount cannot exceed the balance amount");
        }
        
        Payment payment = paymentService.createPayment(invoice, invoice.getPatient(), amount, 
                                                     paymentMethod, paymentReference, null, notes, receivedBy);
        
        // The invoice's payment status is updated in the addPayment method
        invoice.addPayment(payment);
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public Invoice updatePaymentStatus(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + invoiceId));
        
        invoice.updatePaymentStatus();
        
        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public void checkAndUpdateOverdueInvoices() {
        List<Invoice> issuedInvoices = invoiceRepository.findByStatus(Invoice.InvoiceStatus.ISSUED);
        
        for (Invoice invoice : issuedInvoices) {
            if (invoice.isOverdue() && !invoice.getIsCancelled()) {
                invoice.markAsOverdue();
                invoiceRepository.save(invoice);
            }
        }
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        if (invoice.getStatus() != Invoice.InvoiceStatus.DRAFT) {
            throw new RuntimeException("Only invoices in DRAFT status can be deleted");
        }
        
        invoiceRepository.delete(invoice);
    }
}