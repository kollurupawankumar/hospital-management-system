package com.ingestion.laboratory.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.security.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lab_results")
public class LabResult extends BaseEntity {

    @Column(name = "result_number", nullable = false, unique = true)
    private String resultNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @Column(name = "result_date")
    private LocalDateTime resultDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ResultStatus status = ResultStatus.PENDING;

    @Column(name = "clinical_notes", length = 1000)
    private String clinicalNotes;

    @Column(name = "pathologist_notes", length = 1000)
    private String pathologistNotes;

    @Column(name = "is_abnormal")
    private Boolean isAbnormal = false;

    @Column(name = "is_critical")
    private Boolean isCritical = false;

    @Column(name = "critical_informed_to")
    private String criticalInformedTo;

    @Column(name = "critical_informed_date")
    private LocalDateTime criticalInformedDate;

    @Column(name = "report_delivery_method")
    private String reportDeliveryMethod;

    @Column(name = "report_delivery_date")
    private LocalDateTime reportDeliveryDate;

    @OneToMany(mappedBy = "labResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabResultItem> resultItems = new ArrayList<>();

    public enum ResultStatus {
        PENDING, IN_PROCESS, COMPLETED, VERIFIED, APPROVED, DELIVERED
    }

    @PrePersist
    protected void onCreate() {
        if (resultNumber == null || resultNumber.isEmpty()) {
            // Generate result number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            resultNumber = "RES-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
    }

    // Getters and Setters
    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public LabOrder getLabOrder() {
        return labOrder;
    }

    public void setLabOrder(LabOrder labOrder) {
        this.labOrder = labOrder;
    }

    public LocalDateTime getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDateTime resultDate) {
        this.resultDate = resultDate;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDateTime verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public String getPathologistNotes() {
        return pathologistNotes;
    }

    public void setPathologistNotes(String pathologistNotes) {
        this.pathologistNotes = pathologistNotes;
    }

    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean abnormal) {
        isAbnormal = abnormal;
    }

    public Boolean getIsCritical() {
        return isCritical;
    }

    public void setIsCritical(Boolean critical) {
        isCritical = critical;
    }

    public String getCriticalInformedTo() {
        return criticalInformedTo;
    }

    public void setCriticalInformedTo(String criticalInformedTo) {
        this.criticalInformedTo = criticalInformedTo;
    }

    public LocalDateTime getCriticalInformedDate() {
        return criticalInformedDate;
    }

    public void setCriticalInformedDate(LocalDateTime criticalInformedDate) {
        this.criticalInformedDate = criticalInformedDate;
    }

    public String getReportDeliveryMethod() {
        return reportDeliveryMethod;
    }

    public void setReportDeliveryMethod(String reportDeliveryMethod) {
        this.reportDeliveryMethod = reportDeliveryMethod;
    }

    public LocalDateTime getReportDeliveryDate() {
        return reportDeliveryDate;
    }

    public void setReportDeliveryDate(LocalDateTime reportDeliveryDate) {
        this.reportDeliveryDate = reportDeliveryDate;
    }

    public List<LabResultItem> getResultItems() {
        return resultItems;
    }

    public void setResultItems(List<LabResultItem> resultItems) {
        this.resultItems = resultItems;
    }

    // Helper methods
    public void addResultItem(LabResultItem resultItem) {
        resultItems.add(resultItem);
        resultItem.setLabResult(this);
    }

    public void removeResultItem(LabResultItem resultItem) {
        resultItems.remove(resultItem);
        resultItem.setLabResult(null);
    }

    public void markAsInProcess(User performedBy) {
        this.performedBy = performedBy;
        this.status = ResultStatus.IN_PROCESS;
    }

    public void markAsCompleted() {
        this.resultDate = LocalDateTime.now();
        this.status = ResultStatus.COMPLETED;
    }

    public void markAsVerified(User verifiedBy) {
        this.verifiedBy = verifiedBy;
        this.verifiedDate = LocalDateTime.now();
        this.status = ResultStatus.VERIFIED;
    }

    public void markAsApproved(User approvedBy, String pathologistNotes) {
        this.approvedBy = approvedBy;
        this.approvedDate = LocalDateTime.now();
        this.pathologistNotes = pathologistNotes;
        this.status = ResultStatus.APPROVED;
        
        // Update the lab order status
        if (this.labOrder != null) {
            this.labOrder.markAsCompleted();
        }
    }

    public void markAsDelivered(String deliveryMethod) {
        this.reportDeliveryMethod = deliveryMethod;
        this.reportDeliveryDate = LocalDateTime.now();
        this.status = ResultStatus.DELIVERED;
    }

    public void markCriticalResult(String informedTo) {
        this.isCritical = true;
        this.criticalInformedTo = informedTo;
        this.criticalInformedDate = LocalDateTime.now();
    }

    public void checkAbnormalAndCriticalFlags() {
        boolean hasAbnormal = false;
        boolean hasCritical = false;
        
        for (LabResultItem item : resultItems) {
            if (item.getIsAbnormal()) {
                hasAbnormal = true;
            }
            if (item.getIsCritical()) {
                hasCritical = true;
            }
            
            if (hasAbnormal && hasCritical) {
                break;
            }
        }
        
        this.isAbnormal = hasAbnormal;
        this.isCritical = hasCritical;
    }
}