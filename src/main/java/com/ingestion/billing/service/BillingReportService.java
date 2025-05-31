package com.ingestion.billing.service;

import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.patient.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BillingReportService {

    // Daily Collection Reports
    Map<String, Object> getDailyCollectionReport(LocalDate date);
    
    Map<String, Object> getDailyCollectionReportByPaymentMethod(LocalDate date);
    
    // Revenue Reports
    Map<String, Object> getRevenueReportByPeriod(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getRevenueReportByInvoiceType(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getMonthlyRevenueReport(int year);
    
    // Outstanding Reports
    List<Invoice> getOutstandingInvoicesReport();
    
    Map<String, Object> getAgingAnalysisReport();
    
    // Patient Financial Reports
    Map<String, Object> getPatientFinancialSummary(Long patientId);
    
    List<Invoice> getPatientInvoiceHistory(Long patientId);
    
    List<Payment> getPatientPaymentHistory(Long patientId);
    
    // Insurance Reports
    Map<String, Object> getInsuranceClaimsReport(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getInsuranceClaimSettlementReport(LocalDate startDate, LocalDate endDate);
    
    // Tax Reports
    Map<String, Object> getTaxCollectionReport(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getTaxCollectionReportByTaxType(LocalDate startDate, LocalDate endDate);
    
    // Discount Reports
    Map<String, Object> getDiscountReport(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getDiscountReportByDiscountType(LocalDate startDate, LocalDate endDate);
    
    // Refund Reports
    Map<String, Object> getRefundReport(LocalDate startDate, LocalDate endDate);
    
    // Department-wise Revenue Reports
    Map<String, Object> getDepartmentWiseRevenueReport(LocalDate startDate, LocalDate endDate);
    
    // Service-wise Revenue Reports
    Map<String, Object> getServiceWiseRevenueReport(LocalDate startDate, LocalDate endDate);
    
    // Custom Reports
    Map<String, Object> generateCustomReport(LocalDate startDate, LocalDate endDate, 
                                           List<String> groupByFields, List<String> filterCriteria);
    
    // Export Reports
    byte[] exportReportToExcel(String reportType, LocalDate startDate, LocalDate endDate, Map<String, Object> parameters);
    
    byte[] exportReportToPdf(String reportType, LocalDate startDate, LocalDate endDate, Map<String, Object> parameters);
}