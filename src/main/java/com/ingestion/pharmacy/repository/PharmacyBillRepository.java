package com.ingestion.pharmacy.repository;

import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.PharmacyBill;
import com.ingestion.pharmacy.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyBillRepository extends JpaRepository<PharmacyBill, Long> {

    Optional<PharmacyBill> findByBillNumber(String billNumber);
    
    List<PharmacyBill> findByPatient(Patient patient);
    
    List<PharmacyBill> findByPrescription(Prescription prescription);
    
    List<PharmacyBill> findByPaymentStatus(PharmacyBill.PaymentStatus paymentStatus);
    
    List<PharmacyBill> findByBillDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT pb FROM PharmacyBill pb WHERE pb.patient.id = :patientId ORDER BY pb.billDate DESC")
    List<PharmacyBill> findByPatientOrderByBillDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT pb FROM PharmacyBill pb WHERE pb.paymentStatus = :status ORDER BY pb.billDate DESC")
    List<PharmacyBill> findByPaymentStatusOrderByBillDateDesc(@Param("status") PharmacyBill.PaymentStatus status);
    
    @Query("SELECT pb FROM PharmacyBill pb WHERE pb.billDate >= :startDate ORDER BY pb.billDate DESC")
    Page<PharmacyBill> findRecentBills(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT SUM(pb.grandTotal) FROM PharmacyBill pb WHERE pb.paymentStatus = 'PAID' AND pb.billDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueForPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(pb) FROM PharmacyBill pb WHERE pb.paymentStatus = :status")
    Long countByPaymentStatus(@Param("status") PharmacyBill.PaymentStatus status);
    
    @Query("SELECT COUNT(pb) FROM PharmacyBill pb WHERE pb.billDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}