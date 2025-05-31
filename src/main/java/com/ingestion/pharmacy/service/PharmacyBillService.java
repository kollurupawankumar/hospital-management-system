package com.ingestion.pharmacy.service;

import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Medicine;
import com.ingestion.pharmacy.model.PharmacyBill;
import com.ingestion.pharmacy.model.PharmacyBillItem;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PharmacyBillService {

    PharmacyBill savePharmacyBill(PharmacyBill pharmacyBill);
    
    Optional<PharmacyBill> findById(Long id);
    
    Optional<PharmacyBill> findByBillNumber(String billNumber);
    
    List<PharmacyBill> findAll();
    
    List<PharmacyBill> findByPatient(Patient patient);
    
    List<PharmacyBill> findByPrescription(Prescription prescription);
    
    List<PharmacyBill> findByPaymentStatus(PharmacyBill.PaymentStatus paymentStatus);
    
    List<PharmacyBill> findByBillDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<PharmacyBill> findByPatientOrderByBillDateDesc(Long patientId);
    
    List<PharmacyBill> findByPaymentStatusOrderByBillDateDesc(PharmacyBill.PaymentStatus status);
    
    Page<PharmacyBill> findRecentBills(LocalDateTime startDate, Pageable pageable);
    
    Double getTotalRevenueForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    Long countByPaymentStatus(PharmacyBill.PaymentStatus status);
    
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    PharmacyBill createBillFromPrescription(Prescription prescription, User createdBy);
    
    PharmacyBill createBill(Patient patient, User createdBy, List<PharmacyBillItem> items);
    
    PharmacyBill addItemToBill(Long billId, Medicine medicine, Integer quantity, 
                              BigDecimal unitPrice, BigDecimal discountPercentage, 
                              BigDecimal taxPercentage, String batchNumber);
    
    PharmacyBill removeItemFromBill(Long billId, Long itemId);
    
    PharmacyBill applyDiscount(Long id, BigDecimal discountAmount);
    
    PharmacyBill markAsPaid(Long id, String paymentMethod, String paymentReference);
    
    PharmacyBill markAsPartiallyPaid(Long id, String paymentMethod, String paymentReference);
    
    PharmacyBill cancel(Long id);
    
    void deleteBill(Long id);
}