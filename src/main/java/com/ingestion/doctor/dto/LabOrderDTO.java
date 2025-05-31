package com.ingestion.doctor.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LabOrderDTO {
    
    private Long id;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    private String patientName;
    
    private String doctorName;
    
    private LocalDate orderDate;
    
    private String clinicalNotes;
    
    private String orderPriority;
    
    private String orderStatus;
    
    private Boolean isFasting;
    
    private String specialInstructions;
    
    private List<LabTestDTO> labTests = new ArrayList<>();
    
    public LabOrderDTO() {
    }
    
    public LabOrderDTO(Long id, Long patientId, Long doctorId, String patientName, String doctorName,
                      LocalDate orderDate, String clinicalNotes, String orderPriority, String orderStatus,
                      Boolean isFasting, String specialInstructions, List<LabTestDTO> labTests) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.orderDate = orderDate;
        this.clinicalNotes = clinicalNotes;
        this.orderPriority = orderPriority;
        this.orderStatus = orderStatus;
        this.isFasting = isFasting;
        this.specialInstructions = specialInstructions;
        if (labTests != null) {
            this.labTests = labTests;
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    
    public String getPatientName() {
        return patientName;
    }
    
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getClinicalNotes() {
        return clinicalNotes;
    }
    
    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }
    
    public String getOrderPriority() {
        return orderPriority;
    }
    
    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public Boolean getIsFasting() {
        return isFasting;
    }
    
    public void setIsFasting(Boolean isFasting) {
        this.isFasting = isFasting;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    public List<LabTestDTO> getLabTests() {
        return labTests;
    }
    
    public void setLabTests(List<LabTestDTO> labTests) {
        this.labTests = labTests != null ? labTests : new ArrayList<>();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabOrderDTO that = (LabOrderDTO) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(patientId, that.patientId) &&
               Objects.equals(doctorId, that.doctorId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, doctorId);
    }
    
    @Override
    public String toString() {
        return "LabOrderDTO{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", patientName='" + patientName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", orderDate=" + orderDate +
                ", orderStatus='" + orderStatus + '\'' +
                ", labTests=" + labTests.size() +
                '}';
    }
}
