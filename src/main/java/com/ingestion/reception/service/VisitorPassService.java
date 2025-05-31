package com.ingestion.reception.service;

import com.ingestion.reception.dto.VisitorPassDTO;
import com.ingestion.reception.model.VisitorPass;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorPassService {
    
    /**
     * Create a new visitor pass
     * @param visitorPassDTO the visitor pass data
     * @return the created visitor pass
     */
    VisitorPassDTO createVisitorPass(VisitorPassDTO visitorPassDTO);
    
    /**
     * Get a visitor pass by ID
     * @param id the visitor pass ID
     * @return the visitor pass
     */
    VisitorPassDTO getVisitorPassById(Long id);
    
    /**
     * Get a visitor pass by pass number
     * @param passNumber the pass number
     * @return the visitor pass
     */
    VisitorPassDTO getVisitorPassByNumber(String passNumber);
    
    /**
     * Get visitor passes for a patient
     * @param patientId the patient ID
     * @return list of visitor passes
     */
    List<VisitorPassDTO> getVisitorPassesByPatientId(Long patientId);
    
    /**
     * Get visitor passes by visitor name
     * @param visitorName the visitor name
     * @return list of visitor passes
     */
    List<VisitorPassDTO> getVisitorPassesByVisitorName(String visitorName);
    
    /**
     * Get visitor passes by visitor phone
     * @param visitorPhone the visitor phone
     * @return list of visitor passes
     */
    List<VisitorPassDTO> getVisitorPassesByVisitorPhone(String visitorPhone);
    
    /**
     * Get visitor passes by issue date range
     * @param startTime the start time
     * @param endTime the end time
     * @return list of visitor passes
     */
    List<VisitorPassDTO> getVisitorPassesByIssueDateRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Get visitor passes by status and issue date range
     * @param status the pass status
     * @param startTime the start time
     * @param endTime the end time
     * @return list of visitor passes
     */
    List<VisitorPassDTO> getVisitorPassesByStatusAndIssueDateRange(
            VisitorPass.PassStatus status, 
            LocalDateTime startTime, 
            LocalDateTime endTime);
    
    /**
     * Get active visitor passes for a patient
     * @param patientId the patient ID
     * @return list of active visitor passes
     */
    List<VisitorPassDTO> getActiveVisitorPassesByPatientId(Long patientId);
    
    /**
     * Update a visitor pass
     * @param id the visitor pass ID
     * @param visitorPassDTO the updated visitor pass data
     * @return the updated visitor pass
     */
    VisitorPassDTO updateVisitorPass(Long id, VisitorPassDTO visitorPassDTO);
    
    /**
     * Check in a visitor
     * @param id the visitor pass ID
     * @return the updated visitor pass
     */
    VisitorPassDTO checkInVisitor(Long id);
    
    /**
     * Check out a visitor
     * @param id the visitor pass ID
     * @return the updated visitor pass
     */
    VisitorPassDTO checkOutVisitor(Long id);
    
    /**
     * Cancel a visitor pass
     * @param id the visitor pass ID
     * @return the updated visitor pass
     */
    VisitorPassDTO cancelVisitorPass(Long id);
    
    /**
     * Find and update expired passes
     * @return list of updated visitor passes
     */
    List<VisitorPassDTO> updateExpiredPasses();
    
    /**
     * Generate a pass number
     * @return the generated pass number
     */
    String generatePassNumber();
    
    /**
     * Count active visitors by ward
     * @param wardNumber the ward number
     * @return the count of active visitors
     */
    long countActiveVisitorsByWard(String wardNumber);
}