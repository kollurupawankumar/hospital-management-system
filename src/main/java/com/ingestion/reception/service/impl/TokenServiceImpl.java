package com.ingestion.reception.service.impl;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.doctor.repository.DoctorRepository;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.repository.PatientRepository;
import com.ingestion.reception.dto.TokenDTO;
import com.ingestion.reception.model.Token;
import com.ingestion.reception.repository.TokenRepository;
import com.ingestion.reception.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public TokenServiceImpl(
            TokenRepository tokenRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        this.tokenRepository = tokenRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    @Transactional
    public TokenDTO createToken(TokenDTO tokenDTO) {
        Patient patient = patientRepository.findById(tokenDTO.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + tokenDTO.getPatientId()));
        
        Doctor doctor = doctorRepository.findById(tokenDTO.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + tokenDTO.getDoctorId()));
        
        Token token = new Token();
        token.setPatient(patient);
        token.setDoctor(doctor);
        token.setTokenDate(LocalDate.now());
        token.setIssueTime(LocalDateTime.now());
        token.setTokenType(tokenDTO.getTokenType());
        token.setTokenStatus(Token.TokenStatus.WAITING);
        token.setPriority(tokenDTO.getPriority());
        token.setDepartment(tokenDTO.getDepartment());
        token.setCounterNumber(tokenDTO.getCounterNumber());
        token.setNotes(tokenDTO.getNotes());
        
        // Generate token number
        token.setTokenNumber(generateTokenNumber(tokenDTO.getTokenType(), tokenDTO.getDepartment(), token.getTokenDate()));
        
        // Calculate expected service time based on average wait time
        Double avgWaitTime = calculateAverageWaitTime(token.getTokenDate(), token.getDepartment());
        if (avgWaitTime != null) {
            token.setExpectedServiceTime(token.getIssueTime().plusMinutes(avgWaitTime.longValue()));
        }
        
        Token savedToken = tokenRepository.save(token);
        
        return convertToDTO(savedToken);
    }

    @Override
    public TokenDTO getTokenById(Long id) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with ID: " + id));
        
        return convertToDTO(token);
    }

    @Override
    public TokenDTO getTokenByNumber(String tokenNumber) {
        Token token = tokenRepository.findByTokenNumber(tokenNumber)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with number: " + tokenNumber));
        
        return convertToDTO(token);
    }

    @Override
    public List<TokenDTO> getTokensByPatientId(Long patientId) {
        List<Token> tokens = tokenRepository.findByPatientId(patientId);
        
        return tokens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TokenDTO> getTokensByDoctorAndDate(Long doctorId, LocalDate date) {
        List<Token> tokens = tokenRepository.findByDoctorIdAndTokenDate(doctorId, date);
        
        return tokens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TokenDTO> getTokensByDateAndDepartment(LocalDate date, String department) {
        List<Token> tokens = tokenRepository.findByTokenDateAndDepartmentOrderByIssueTimeAsc(date, department);
        
        return tokens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TokenDTO> getActiveTokensByDate(LocalDate date) {
        List<Token> tokens = tokenRepository.findActiveTokensByDateOrderByPriorityAndTime(date);
        
        return tokens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TokenDTO> getActiveTokensByDateAndDepartment(LocalDate date, String department) {
        List<Token> tokens = tokenRepository.findActiveTokensByDateAndDepartmentOrderByPriorityAndTime(date, department);
        
        return tokens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TokenDTO> getActiveTokensByDoctorAndDate(Long doctorId, LocalDate date) {
        List<Token> tokens = tokenRepository.findActiveTokensByDoctorAndDateOrderByPriorityAndTime(doctorId, date);
        
        return tokens.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TokenDTO updateToken(Long id, TokenDTO tokenDTO) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with ID: " + id));
        
        token.setTokenStatus(tokenDTO.getTokenStatus());
        token.setPriority(tokenDTO.getPriority());
        token.setCounterNumber(tokenDTO.getCounterNumber());
        token.setNotes(tokenDTO.getNotes());
        
        Token updatedToken = tokenRepository.save(token);
        
        return convertToDTO(updatedToken);
    }

    @Override
    @Transactional
    public TokenDTO updateTokenStatus(Long id, Token.TokenStatus status) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with ID: " + id));
        
        token.setTokenStatus(status);
        
        if (status == Token.TokenStatus.CALLED) {
            token.setActualServiceTime(LocalDateTime.now());
        } else if (status == Token.TokenStatus.COMPLETED) {
            token.setCompletionTime(LocalDateTime.now());
        }
        
        Token updatedToken = tokenRepository.save(token);
        
        return convertToDTO(updatedToken);
    }

    @Override
    @Transactional
    public TokenDTO callNextToken(Long doctorId, LocalDate date) {
        List<Token> activeTokens = tokenRepository.findActiveTokensByDoctorAndDateOrderByPriorityAndTime(doctorId, date);
        
        if (activeTokens.isEmpty()) {
            throw new IllegalStateException("No active tokens available for doctor on " + date);
        }
        
        Token nextToken = activeTokens.get(0);
        nextToken.setTokenStatus(Token.TokenStatus.CALLED);
        nextToken.setActualServiceTime(LocalDateTime.now());
        
        Token updatedToken = tokenRepository.save(nextToken);
        
        return convertToDTO(updatedToken);
    }

    @Override
    @Transactional
    public TokenDTO callNextTokenForDepartment(String department, LocalDate date) {
        List<Token> activeTokens = tokenRepository.findActiveTokensByDateAndDepartmentOrderByPriorityAndTime(date, department);
        
        if (activeTokens.isEmpty()) {
            throw new IllegalStateException("No active tokens available for department on " + date);
        }
        
        Token nextToken = activeTokens.get(0);
        nextToken.setTokenStatus(Token.TokenStatus.CALLED);
        nextToken.setActualServiceTime(LocalDateTime.now());
        
        Token updatedToken = tokenRepository.save(nextToken);
        
        return convertToDTO(updatedToken);
    }

    @Override
    @Transactional
    public TokenDTO completeToken(Long id) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with ID: " + id));
        
        token.setTokenStatus(Token.TokenStatus.COMPLETED);
        token.setCompletionTime(LocalDateTime.now());
        
        Token updatedToken = tokenRepository.save(token);
        
        return convertToDTO(updatedToken);
    }

    @Override
    @Transactional
    public TokenDTO cancelToken(Long id) {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Token not found with ID: " + id));
        
        token.setTokenStatus(Token.TokenStatus.CANCELLED);
        
        Token updatedToken = tokenRepository.save(token);
        
        return convertToDTO(updatedToken);
    }

    @Override
    public String generateTokenNumber(Token.TokenType tokenType, String department, LocalDate date) {
        String prefix = "";
        
        switch (tokenType) {
            case WALK_IN:
                prefix = "W";
                break;
            case APPOINTMENT:
                prefix = "A";
                break;
            case EMERGENCY:
                prefix = "E";
                break;
            case FOLLOW_UP:
                prefix = "F";
                break;
            case LAB_TEST:
                prefix = "L";
                break;
            case PHARMACY:
                prefix = "P";
                break;
            default:
                prefix = "T";
        }
        
        // Add department code (first 2 letters)
        if (department != null && !department.isEmpty()) {
            prefix += department.substring(0, Math.min(2, department.length())).toUpperCase();
        }
        
        // Add date code
        prefix += date.format(DateTimeFormatter.ofPattern("yyMMdd"));
        
        // Get last token number with this prefix
        String lastTokenNumber = tokenRepository.findLastTokenNumberWithPrefixForDate(prefix, date);
        
        int sequence = 1;
        if (lastTokenNumber != null) {
            String sequenceStr = lastTokenNumber.substring(prefix.length());
            sequence = Integer.parseInt(sequenceStr) + 1;
        }
        
        return prefix + String.format("%03d", sequence);
    }

    @Override
    public long countTokensByDateAndDepartment(LocalDate date, String department) {
        return tokenRepository.countTokensByDateAndDepartment(date, department);
    }

    @Override
    public Double calculateAverageWaitTime(LocalDate date, String department) {
        return tokenRepository.calculateAverageWaitTimeInMinutes(date, department);
    }
    
    private TokenDTO convertToDTO(Token token) {
        TokenDTO dto = new TokenDTO();
        dto.setId(token.getId());
        dto.setTokenNumber(token.getTokenNumber());
        dto.setPatientId(token.getPatient().getId());
        dto.setPatientName(token.getPatient().getFirstName() + " " + token.getPatient().getLastName());
        dto.setDoctorId(token.getDoctor().getId());
        dto.setDoctorName("Dr. " + token.getDoctor().getFirstName() + " " + token.getDoctor().getLastName());
        dto.setTokenDate(token.getTokenDate());
        dto.setIssueTime(token.getIssueTime());
        dto.setExpectedServiceTime(token.getExpectedServiceTime());
        dto.setActualServiceTime(token.getActualServiceTime());
        dto.setCompletionTime(token.getCompletionTime());
        dto.setTokenType(token.getTokenType());
        dto.setTokenStatus(token.getTokenStatus());
        dto.setPriority(token.getPriority());
        dto.setDepartment(token.getDepartment());
        dto.setCounterNumber(token.getCounterNumber());
        dto.setNotes(token.getNotes());
        
        // Calculate estimated wait time
        if (token.getExpectedServiceTime() != null && token.getTokenStatus() == Token.TokenStatus.WAITING) {
            long waitMinutes = java.time.Duration.between(LocalDateTime.now(), token.getExpectedServiceTime()).toMinutes();
            dto.setEstimatedWaitTime(waitMinutes + " min");
        }
        
        // Format status for display
        switch (token.getTokenStatus()) {
            case WAITING:
                dto.setStatusDisplay("Waiting");
                break;
            case CALLED:
                dto.setStatusDisplay("Called");
                break;
            case IN_PROGRESS:
                dto.setStatusDisplay("In Progress");
                break;
            case COMPLETED:
                dto.setStatusDisplay("Completed");
                break;
            case NO_SHOW:
                dto.setStatusDisplay("No Show");
                break;
            case CANCELLED:
                dto.setStatusDisplay("Cancelled");
                break;
            case TRANSFERRED:
                dto.setStatusDisplay("Transferred");
                break;
            default:
                dto.setStatusDisplay(token.getTokenStatus().toString());
        }
        
        // Format priority for display
        switch (token.getPriority()) {
            case LOW:
                dto.setPriorityDisplay("Low");
                break;
            case NORMAL:
                dto.setPriorityDisplay("Normal");
                break;
            case HIGH:
                dto.setPriorityDisplay("High");
                break;
            case URGENT:
                dto.setPriorityDisplay("Urgent");
                break;
            default:
                dto.setPriorityDisplay(token.getPriority().toString());
        }
        
        return dto;
    }
}