package com.ingestion.laboratory.model;

import com.ingestion.common.model.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lab_analyzers")
public class LabAnalyzer extends BaseEntity {

    @Column(name = "analyzer_id", nullable = false, unique = true)
    private String analyzerId;

    @Column(name = "analyzer_name", nullable = false)
    private String analyzerName;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "model")
    private String model;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "location")
    private String location;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port")
    private Integer port;

    @Column(name = "connection_type")
    private String connectionType;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "last_maintenance_date")
    private LocalDateTime lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;

    @Column(name = "last_calibration_date")
    private LocalDateTime lastCalibrationDate;

    @Column(name = "next_calibration_date")
    private LocalDateTime nextCalibrationDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AnalyzerStatus status = AnalyzerStatus.ACTIVE;

    @Column(name = "notes", length = 1000)
    private String notes;

    @ManyToMany
    @JoinTable(
        name = "analyzer_test_mappings",
        joinColumns = @JoinColumn(name = "analyzer_id"),
        inverseJoinColumns = @JoinColumn(name = "lab_test_id")
    )
    private List<LabTest> supportedTests = new ArrayList<>();

    public enum AnalyzerStatus {
        ACTIVE, MAINTENANCE, CALIBRATION, INACTIVE, OUT_OF_ORDER
    }

    // Getters and Setters
    public String getAnalyzerId() {
        return analyzerId;
    }

    public void setAnalyzerId(String analyzerId) {
        this.analyzerId = analyzerId;
    }

    public String getAnalyzerName() {
        return analyzerName;
    }

    public void setAnalyzerName(String analyzerName) {
        this.analyzerName = analyzerName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public LocalDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDateTime lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public LocalDateTime getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDateTime nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public LocalDateTime getLastCalibrationDate() {
        return lastCalibrationDate;
    }

    public void setLastCalibrationDate(LocalDateTime lastCalibrationDate) {
        this.lastCalibrationDate = lastCalibrationDate;
    }

    public LocalDateTime getNextCalibrationDate() {
        return nextCalibrationDate;
    }

    public void setNextCalibrationDate(LocalDateTime nextCalibrationDate) {
        this.nextCalibrationDate = nextCalibrationDate;
    }

    public AnalyzerStatus getStatus() {
        return status;
    }

    public void setStatus(AnalyzerStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<LabTest> getSupportedTests() {
        return supportedTests;
    }

    public void setSupportedTests(List<LabTest> supportedTests) {
        this.supportedTests = supportedTests;
    }

    // Helper methods
    public void addSupportedTest(LabTest test) {
        supportedTests.add(test);
    }

    public void removeSupportedTest(LabTest test) {
        supportedTests.remove(test);
    }

    public void markAsMaintenance() {
        this.status = AnalyzerStatus.MAINTENANCE;
    }

    public void markAsCalibration() {
        this.status = AnalyzerStatus.CALIBRATION;
    }

    public void markAsActive() {
        this.status = AnalyzerStatus.ACTIVE;
    }

    public void markAsInactive() {
        this.status = AnalyzerStatus.INACTIVE;
    }

    public void markAsOutOfOrder() {
        this.status = AnalyzerStatus.OUT_OF_ORDER;
    }

    public void performMaintenance(LocalDateTime maintenanceDate, LocalDateTime nextMaintenanceDate) {
        this.lastMaintenanceDate = maintenanceDate;
        this.nextMaintenanceDate = nextMaintenanceDate;
        this.status = AnalyzerStatus.ACTIVE;
    }

    public void performCalibration(LocalDateTime calibrationDate, LocalDateTime nextCalibrationDate) {
        this.lastCalibrationDate = calibrationDate;
        this.nextCalibrationDate = nextCalibrationDate;
        this.status = AnalyzerStatus.ACTIVE;
    }
}