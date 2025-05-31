package com.ingestion.billing.service.impl;

import com.ingestion.common.model.billing.Invoice;
import com.ingestion.common.model.billing.Payment;
import com.ingestion.billing.repository.PaymentRepository;
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
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Optional<Payment> findByPaymentNumber(String paymentNumber) {
        return paymentRepository.findByPaymentNumber(paymentNumber);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> findByInvoice(Invoice invoice) {
        return paymentRepository.findByInvoice(invoice);
    }

    @Override
    public List<Payment> findByPatient(Patient patient) {
        return paymentRepository.findByPatient(patient);
    }

    @Override
    public List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    @Override
    public List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    @Override
    public List<Payment> findByIsRefunded(Boolean isRefunded) {
        return paymentRepository.findByIsRefunded(isRefunded);
    }

    @Override
    public List<Payment> findByPatientOrderByPaymentDateDesc(Long patientId) {
        return paymentRepository.findByPatientOrderByPaymentDateDesc(patientId);
    }

    @Override
    public List<Payment> findByInvoiceOrderByPaymentDateDesc(Long invoiceId) {
        return paymentRepository.findByInvoiceOrderByPaymentDateDesc(invoiceId);
    }

    @Override
    public List<Payment> findByPaymentMethodOrderByPaymentDateDesc(Payment.PaymentMethod paymentMethod) {
        return paymentRepository.findByPaymentMethodOrderByPaymentDateDesc(paymentMethod);
    }

    @Override
    public Page<Payment> findRecentPayments(LocalDateTime startDate, Pageable pageable) {
        return paymentRepository.findRecentPayments(startDate, pageable);
    }

    @Override
    public Double getTotalPaymentAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.getTotalPaymentAmountForPeriod(startDate, endDate);
    }

    @Override
    public Double getTotalRefundAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.getTotalRefundAmountForPeriod(startDate, endDate);
    }

    @Override
    public Long countByPaymentMethod(Payment.PaymentMethod paymentMethod) {
        return paymentRepository.countByPaymentMethod(paymentMethod);
    }

    @Override
    public Long countRefundedPayments() {
        return paymentRepository.countRefundedPayments();
    }

    @Override
    public Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.countByDateRange(startDate, endDate);
    }

    @Override
    public Payment createPayment(Invoice invoice, Patient patient, BigDecimal amount, 
                               Payment.PaymentMethod paymentMethod, String paymentReference, 
                               String transactionId, String notes, User receivedBy) {
        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setPatient(patient);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentReference(paymentReference);
        payment.setTransactionId(transactionId);
        payment.setNotes(notes);
        payment.setReceivedBy(receivedBy);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        
        return savePayment(payment);
    }

    @Override
    public Payment createPaymentWithoutInvoice(Patient patient, BigDecimal amount, 
                                             Payment.PaymentMethod paymentMethod, String paymentReference, 
                                             String transactionId, String notes, User receivedBy) {
        return createPayment(null, patient, amount, paymentMethod, paymentReference, 
                           transactionId, notes, receivedBy);
    }

    @Override
    public Payment refundPayment(Long paymentId, User refundedBy, String reason, String refundReference) {
        Optional<Payment> paymentOpt = findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            if (!payment.getIsRefunded()) {
                payment.setIsRefunded(true);
                payment.setRefundedBy(refundedBy);
                payment.setRefundReason(reason);
                payment.setRefundReference(refundReference);
                payment.setRefundDate(LocalDateTime.now());
                payment.setStatus(Payment.PaymentStatus.REFUNDED);
                
                return savePayment(payment);
            } else {
                throw new IllegalStateException("Payment is already refunded");
            }
        } else {
            throw new IllegalArgumentException("Payment not found with id: " + paymentId);
        }
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}