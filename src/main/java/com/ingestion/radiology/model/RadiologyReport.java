package com.ingestion.radiology.model;

import com.ingestion.common.model.BaseEntity;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.security.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "radiology_reports")
public class RadiologyReport extends BaseEntity {

    @Column(name = "report_number", nullable = false, unique = true)
    private String reportNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiology_order_id", nullable = false)
    private RadiologyOrder radiologyOrder;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    @Column(name = "findings", length = 2000)
    private String findings;

    @Column(name = "impression", length = 1000)
    private String impression;

    @Column(name = "recommendations", length = 1000)
    private String recommendations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiologist_id")
    private Doctor radiologist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User technician;

    @Column(name = "verified_date")
    private LocalDateTime verifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

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

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RadiologyImage> images = new ArrayList<>();

    public enum ReportStatus {
        PENDING, DRAFT, COMPLETED, VERIFIED, DELIVERED
    }

    @PrePersist
    protected void onCreate() {
        if (reportNumber == null || reportNumber.isEmpty()) {
            // Generate report number
            String datePrefix = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            reportNumber = "RAD-REP-" + datePrefix + "-" + System.nanoTime() % 10000;
        }
    }

    // Getters and Setters
    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public RadiologyOrder getRadiologyOrder() {
        return radiologyOrder;
    }

    public void setRadiologyOrder(RadiologyOrder radiologyOrder) {
        this.radiologyOrder = radiologyOrder;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public Doctor getRadiologist() {
        return radiologist;
    }

    public void setRadiologist(Doctor radiologist) {
        this.radiologist = radiologist;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
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

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
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

    public List<RadiologyImage> getImages() {
        return images;
    }

    public void setImages(List<RadiologyImage> images) {
        this.images = images;
    }

    // Helper methods
    public void addImage(RadiologyImage image) {
        images.add(image);
        image.setReport(this);
    }

    public void removeImage(RadiologyImage image) {
        images.remove(image);
        image.setReport(null);
    }

    public void saveDraft() {
        this.status = ReportStatus.DRAFT;
    }

    public void complete() {
        this.reportDate = LocalDateTime.now();
        this.status = ReportStatus.COMPLETED;
        
        // Update the order status
        if (this.radiologyOrder != null) {
            this.radiologyOrder.completeProcedure();
        }
    }

    public void verify(User verifier) {
        this.verifiedBy = verifier;
        this.verifiedDate = LocalDateTime.now();
        this.status = ReportStatus.VERIFIED;
    }

    public void deliver(String deliveryMethod) {
        this.reportDeliveryMethod = deliveryMethod;
        this.reportDeliveryDate = LocalDateTime.now();
        this.status = ReportStatus.DELIVERED;
    }

    public void markAsCritical(String informedTo) {
        this.isCritical = true;
        this.criticalInformedTo = informedTo;
        this.criticalInformedDate = LocalDateTime.now();
    }
}