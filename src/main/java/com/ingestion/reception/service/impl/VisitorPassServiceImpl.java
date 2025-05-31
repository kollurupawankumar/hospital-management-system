package com.ingestion.reception.service.impl;

import com.ingestion.patient.model.Patient;
import com.ingestion.patient.repository.PatientRepository;
import com.ingestion.reception.dto.VisitorPassDTO;
import com.ingestion.reception.model.VisitorPass;
import com.ingestion.reception.repository.VisitorPassRepository;
import com.ingestion.reception.service.VisitorPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitorPassServiceImpl implements VisitorPassService {

    @Autowired
    private VisitorPassRepository visitorPassRepository;
    @Autowired
    private PatientRepository patientRepository;


    @Override
    @Transactional
    public VisitorPassDTO createVisitorPass(VisitorPassDTO visitorPassDTO) {
        Patient patient = patientRepository.findById(visitorPassDTO.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + visitorPassDTO.getPatientId()));
        
        VisitorPass visitorPass = new VisitorPass();
        visitorPass.setPassNumber(generatePassNumber());
        visitorPass.setVisitorName(visitorPassDTO.getVisitorName());
        visitorPass.setVisitorPhone(visitorPassDTO.getVisitorPhone());
        visitorPass.setVisitorIdType(visitorPassDTO.getVisitorIdType());
        visitorPass.setVisitorIdNumber(visitorPassDTO.getVisitorIdNumber());
        visitorPass.setPatient(patient);
        visitorPass.setRelationshipToPatient(visitorPassDTO.getRelationshipToPatient());
        
        LocalDateTime now = LocalDateTime.now();
        visitorPass.setIssueTime(now);
        
        // Default expiry time is 4 hours from issue time
        visitorPass.setExpiryTime(now.plusHours(4));
        
        visitorPass.setIssuedBy(visitorPassDTO.getIssuedBy());
        visitorPass.setPassStatus(VisitorPass.PassStatus.ACTIVE);
        visitorPass.setWardNumber(visitorPassDTO.getWardNumber());
        visitorPass.setRoomNumber(visitorPassDTO.getRoomNumber());
        visitorPass.setPurpose(visitorPassDTO.getPurpose());
        visitorPass.setNotes(visitorPassDTO.getNotes());
        
        VisitorPass savedVisitorPass = visitorPassRepository.save(visitorPass);
        
        return convertToDTO(savedVisitorPass);
    }

    @Override
    public VisitorPassDTO getVisitorPassById(Long id) {
        VisitorPass visitorPass = visitorPassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor pass not found with ID: " + id));
        
        return convertToDTO(visitorPass);
    }

    @Override
    public VisitorPassDTO getVisitorPassByNumber(String passNumber) {
        VisitorPass visitorPass = visitorPassRepository.findByPassNumber(passNumber)
                .orElseThrow(() -> new EntityNotFoundException("Visitor pass not found with number: " + passNumber));
        
        return convertToDTO(visitorPass);
    }

    @Override
    public List<VisitorPassDTO> getVisitorPassesByPatientId(Long patientId) {
        List<VisitorPass> visitorPasses = visitorPassRepository.findByPatientId(patientId);
        
        return visitorPasses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitorPassDTO> getVisitorPassesByVisitorName(String visitorName) {
        List<VisitorPass> visitorPasses = visitorPassRepository.findByVisitorNameContainingIgnoreCase(visitorName);
        
        return visitorPasses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitorPassDTO> getVisitorPassesByVisitorPhone(String visitorPhone) {
        List<VisitorPass> visitorPasses = visitorPassRepository.findByVisitorPhoneContaining(visitorPhone);
        
        return visitorPasses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitorPassDTO> getVisitorPassesByIssueDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<VisitorPass> visitorPasses = visitorPassRepository.findByIssueDateTimeBetween(startTime, endTime);
        
        return visitorPasses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitorPassDTO> getVisitorPassesByStatusAndIssueDateRange(
            VisitorPass.PassStatus status, 
            LocalDateTime startTime, 
            LocalDateTime endTime) {
        List<VisitorPass> visitorPasses = visitorPassRepository.findByStatusAndIssueDateTimeBetween(
                status, startTime, endTime);
        
        return visitorPasses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitorPassDTO> getActiveVisitorPassesByPatientId(Long patientId) {
        List<VisitorPass> visitorPasses = visitorPassRepository.findActiveVisitorsByPatientId(patientId);
        
        return visitorPasses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VisitorPassDTO updateVisitorPass(Long id, VisitorPassDTO visitorPassDTO) {
        VisitorPass visitorPass = visitorPassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor pass not found with ID: " + id));
        
        visitorPass.setVisitorName(visitorPassDTO.getVisitorName());
        visitorPass.setVisitorPhone(visitorPassDTO.getVisitorPhone());
        visitorPass.setVisitorIdType(visitorPassDTO.getVisitorIdType());
        visitorPass.setVisitorIdNumber(visitorPassDTO.getVisitorIdNumber());
        visitorPass.setRelationshipToPatient(visitorPassDTO.getRelationshipToPatient());
        visitorPass.setExpiryTime(visitorPassDTO.getExpiryTime());
        visitorPass.setWardNumber(visitorPassDTO.getWardNumber());
        visitorPass.setRoomNumber(visitorPassDTO.getRoomNumber());
        visitorPass.setPurpose(visitorPassDTO.getPurpose());
        visitorPass.setNotes(visitorPassDTO.getNotes());
        
        VisitorPass updatedVisitorPass = visitorPassRepository.save(visitorPass);
        
        return convertToDTO(updatedVisitorPass);
    }

    @Override
    @Transactional
    public VisitorPassDTO checkInVisitor(Long id) {
        VisitorPass visitorPass = visitorPassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor pass not found with ID: " + id));
        
        if (visitorPass.getPassStatus() != VisitorPass.PassStatus.ACTIVE) {
            throw new IllegalStateException("Visitor pass is not active");
        }
        
        visitorPass.setPassStatus(VisitorPass.PassStatus.CHECKED_IN);
        visitorPass.setCheckInTime(LocalDateTime.now());
        
        VisitorPass updatedVisitorPass = visitorPassRepository.save(visitorPass);
        
        return convertToDTO(updatedVisitorPass);
    }

    @Override
    @Transactional
    public VisitorPassDTO checkOutVisitor(Long id) {
        VisitorPass visitorPass = visitorPassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor pass not found with ID: " + id));
        
        if (visitorPass.getPassStatus() != VisitorPass.PassStatus.CHECKED_IN) {
            throw new IllegalStateException("Visitor is not checked in");
        }
        
        visitorPass.setPassStatus(VisitorPass.PassStatus.CHECKED_OUT);
        visitorPass.setCheckOutTime(LocalDateTime.now());
        
        VisitorPass updatedVisitorPass = visitorPassRepository.save(visitorPass);
        
        return convertToDTO(updatedVisitorPass);
    }

    @Override
    @Transactional
    public VisitorPassDTO cancelVisitorPass(Long id) {
        VisitorPass visitorPass = visitorPassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor pass not found with ID: " + id));
        
        visitorPass.setPassStatus(VisitorPass.PassStatus.CANCELLED);
        
        VisitorPass updatedVisitorPass = visitorPassRepository.save(visitorPass);
        
        return convertToDTO(updatedVisitorPass);
    }

    @Override
    @Transactional
    public List<VisitorPassDTO> updateExpiredPasses() {
        LocalDateTime now = LocalDateTime.now();
        List<VisitorPass> expiredPasses = visitorPassRepository.findExpiredPasses(now);
        
        for (VisitorPass pass : expiredPasses) {
            pass.setPassStatus(VisitorPass.PassStatus.EXPIRED);
        }
        
        List<VisitorPass> updatedPasses = visitorPassRepository.saveAll(expiredPasses);
        
        return updatedPasses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String generatePassNumber() {
        LocalDate today = LocalDate.now();
        String prefix = "VP-" + today.format(DateTimeFormatter.ofPattern("yyMMdd")) + "-";
        
        String lastPassNumber = visitorPassRepository.findLastPassNumberWithPrefix(prefix);
        
        int sequence = 1;
        if (lastPassNumber != null) {
            String sequenceStr = lastPassNumber.substring(prefix.length());
            sequence = Integer.parseInt(sequenceStr) + 1;
        }
        
        return prefix + String.format("%03d", sequence);
    }

    @Override
    public long countActiveVisitorsByWard(String wardNumber) {
        return visitorPassRepository.countActiveVisitorsByWard(wardNumber);
    }
    
    private VisitorPassDTO convertToDTO(VisitorPass visitorPass) {
        VisitorPassDTO dto = new VisitorPassDTO();
        dto.setId(visitorPass.getId());
        dto.setPassNumber(visitorPass.getPassNumber());
        dto.setVisitorName(visitorPass.getVisitorName());
        dto.setVisitorPhone(visitorPass.getVisitorPhone());
        dto.setVisitorIdType(visitorPass.getVisitorIdType());
        dto.setVisitorIdNumber(visitorPass.getVisitorIdNumber());
        dto.setPatientId(visitorPass.getPatient().getId());
        dto.setPatientName(visitorPass.getPatient().getFirstName() + " " + visitorPass.getPatient().getLastName());
        dto.setRelationshipToPatient(visitorPass.getRelationshipToPatient());
        dto.setIssueTime(visitorPass.getIssueTime());
        dto.setExpiryTime(visitorPass.getExpiryTime());
        dto.setCheckInTime(visitorPass.getCheckInTime());
        dto.setCheckOutTime(visitorPass.getCheckOutTime());
        dto.setIssuedBy(visitorPass.getIssuedBy());
        dto.setPassStatus(visitorPass.getPassStatus());
        dto.setWardNumber(visitorPass.getWardNumber());
        dto.setRoomNumber(visitorPass.getRoomNumber());
        dto.setPurpose(visitorPass.getPurpose());
        dto.setNotes(visitorPass.getNotes());
        
        // Format status for display
        switch (visitorPass.getPassStatus()) {
            case ACTIVE:
                dto.setStatusDisplay("Active");
                break;
            case CHECKED_IN:
                dto.setStatusDisplay("Checked In");
                break;
            case CHECKED_OUT:
                dto.setStatusDisplay("Checked Out");
                break;
            case EXPIRED:
                dto.setStatusDisplay("Expired");
                break;
            case CANCELLED:
                dto.setStatusDisplay("Cancelled");
                break;
            default:
                dto.setStatusDisplay(visitorPass.getPassStatus().toString());
        }
        
        // Calculate validity period
        if (visitorPass.getIssueTime() != null && visitorPass.getExpiryTime() != null) {
            long hours = visitorPass.getIssueTime().until(visitorPass.getExpiryTime(), ChronoUnit.HOURS);
            dto.setValidityPeriod(hours + " hours");
        }
        
        // Check if pass is expired
        if (visitorPass.getExpiryTime() != null) {
            dto.setExpired(visitorPass.getExpiryTime().isBefore(LocalDateTime.now()));
        }
        
        return dto;
    }
}