package com.ingestion.patient.service;

import com.ingestion.patient.model.LabReport;
import com.ingestion.patient.model.LabTestResult;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.repository.LabReportRepository;
import com.ingestion.patient.repository.PatientRepository;
import com.ingestion.common.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LabReportService {
    private static final Logger log = LoggerFactory.getLogger(LabReportService.class);
    @Autowired
    private LabReportRepository labReportRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<LabReport> getAllLabReports() {
        log.debug("Fetching all lab reports");
        return labReportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<LabReport> getLabReportById(Long id) {
        log.debug("Fetching lab report with ID: {}", id);
        return labReportRepository.findById(id);
    }

    @Transactional
    public LabReport saveLabReport(LabReport labReport) {
        log.debug("Saving lab report: {}", labReport);
        
        boolean isNew = labReport.getId() == null;
        LabReport savedReport = labReportRepository.save(labReport);
        
        if (isNew) {
            notificationService.sendLabReportNotification(savedReport);
        }
        
        return savedReport;
    }

    @Transactional
    public void deleteLabReport(Long id) {
        log.debug("Deleting lab report with ID: {}", id);
        labReportRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LabReport> getLabReportsByPatientId(Long patientId) {
        log.debug("Fetching lab reports for patient ID: {}", patientId);
        return labReportRepository.findByPatientIdOrderByDateDesc(patientId);
    }

    @Transactional(readOnly = true)
    public List<LabReport> getLabReportsByPatientIdAndType(Long patientId, String reportType) {
        log.debug("Fetching lab reports for patient ID: {} with type: {}", patientId, reportType);
        return labReportRepository.findByPatientIdAndReportType(patientId, reportType);
    }

    @Transactional(readOnly = true)
    public List<LabReport> getLabReportsByPatientIdAndDateRange(Long patientId, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching lab reports for patient ID: {} between {} and {}", patientId, startDate, endDate);
        return labReportRepository.findByPatientIdAndDateRange(patientId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<LabReport> getAbnormalLabReportsByPatientId(Long patientId) {
        log.debug("Fetching abnormal lab reports for patient ID: {}", patientId);
        return labReportRepository.findAbnormalByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<LabReport> getUnviewedLabReportsByPatientId(Long patientId) {
        log.debug("Fetching unviewed lab reports for patient ID: {}", patientId);
        return labReportRepository.findUnviewedByPatientId(patientId);
    }

    @Transactional
    public LabReport createLabReport(Long patientId, String reportType, String labName, String technicianName, 
                                    String reportSummary, List<LabTestResult> testResults) {
        log.debug("Creating lab report for patient ID: {}", patientId);
        
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            
            LabReport labReport = new LabReport();
            labReport.setPatient(patient);
            labReport.setReportDate(LocalDate.now());
            labReport.setReportType(reportType);
            labReport.setLabName(labName);
            labReport.setTechnicianName(technicianName);
            labReport.setReportSummary(reportSummary);
            labReport.setIsViewedByPatient(false);
            labReport.setIsViewedByDoctor(false);
            
            // Check if any test results are abnormal
            boolean hasAbnormalResults = false;
            for (LabTestResult result : testResults) {
                if (result.getIsAbnormal()) {
                    hasAbnormalResults = true;
                    break;
                }
            }
            labReport.setIsAbnormal(hasAbnormalResults);
            
            LabReport savedReport = labReportRepository.save(labReport);
            
            // Add test results
            for (LabTestResult result : testResults) {
                result.setLabReport(savedReport);
            }
            
            savedReport.setTestResults(testResults);
            savedReport = labReportRepository.save(savedReport);
            
            notificationService.sendLabReportNotification(savedReport);
            
            return savedReport;
        } else {
            log.error("Patient ID: {} not found", patientId);
            throw new IllegalArgumentException("Patient not found");
        }
    }

    @Transactional
    public void markLabReportAsViewedByPatient(Long reportId) {
        log.debug("Marking lab report ID: {} as viewed by patient", reportId);
        
        Optional<LabReport> reportOpt = labReportRepository.findById(reportId);
        
        if (reportOpt.isPresent()) {
            LabReport report = reportOpt.get();
            report.setIsViewedByPatient(true);
            labReportRepository.save(report);
        } else {
            log.error("Lab report ID: {} not found", reportId);
            throw new IllegalArgumentException("Lab report not found");
        }
    }

    @Transactional
    public void markLabReportAsViewedByDoctor(Long reportId) {
        log.debug("Marking lab report ID: {} as viewed by doctor", reportId);
        
        Optional<LabReport> reportOpt = labReportRepository.findById(reportId);
        
        if (reportOpt.isPresent()) {
            LabReport report = reportOpt.get();
            report.setIsViewedByDoctor(true);
            labReportRepository.save(report);
        } else {
            log.error("Lab report ID: {} not found", reportId);
            throw new IllegalArgumentException("Lab report not found");
        }
    }

    @Transactional(readOnly = true)
    public List<String> getAllReportTypes() {
        log.debug("Fetching all report types");
        return labReportRepository.findAllReportTypes();
    }
}