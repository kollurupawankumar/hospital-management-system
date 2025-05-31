package com.ingestion.reception.service;

import com.ingestion.reception.dto.TokenDTO;
import com.ingestion.reception.model.Token;

import java.time.LocalDate;
import java.util.List;

public interface TokenService {
    
    /**
     * Create a new token
     * @param tokenDTO the token data
     * @return the created token
     */
    TokenDTO createToken(TokenDTO tokenDTO);
    
    /**
     * Get a token by ID
     * @param id the token ID
     * @return the token
     */
    TokenDTO getTokenById(Long id);
    
    /**
     * Get a token by token number
     * @param tokenNumber the token number
     * @return the token
     */
    TokenDTO getTokenByNumber(String tokenNumber);
    
    /**
     * Get tokens for a patient
     * @param patientId the patient ID
     * @return list of tokens
     */
    List<TokenDTO> getTokensByPatientId(Long patientId);
    
    /**
     * Get tokens for a doctor on a specific date
     * @param doctorId the doctor ID
     * @param date the date
     * @return list of tokens
     */
    List<TokenDTO> getTokensByDoctorAndDate(Long doctorId, LocalDate date);
    
    /**
     * Get tokens for a department on a specific date
     * @param date the date
     * @param department the department
     * @return list of tokens
     */
    List<TokenDTO> getTokensByDateAndDepartment(LocalDate date, String department);
    
    /**
     * Get active tokens (waiting or called) for a specific date, ordered by priority and time
     * @param date the date
     * @return list of active tokens
     */
    List<TokenDTO> getActiveTokensByDate(LocalDate date);
    
    /**
     * Get active tokens for a department on a specific date, ordered by priority and time
     * @param date the date
     * @param department the department
     * @return list of active tokens
     */
    List<TokenDTO> getActiveTokensByDateAndDepartment(LocalDate date, String department);
    
    /**
     * Get active tokens for a doctor on a specific date, ordered by priority and time
     * @param doctorId the doctor ID
     * @param date the date
     * @return list of active tokens
     */
    List<TokenDTO> getActiveTokensByDoctorAndDate(Long doctorId, LocalDate date);
    
    /**
     * Update a token
     * @param id the token ID
     * @param tokenDTO the updated token data
     * @return the updated token
     */
    TokenDTO updateToken(Long id, TokenDTO tokenDTO);
    
    /**
     * Update token status
     * @param id the token ID
     * @param status the new status
     * @return the updated token
     */
    TokenDTO updateTokenStatus(Long id, Token.TokenStatus status);
    
    /**
     * Call the next token for a doctor
     * @param doctorId the doctor ID
     * @param date the date
     * @return the called token
     */
    TokenDTO callNextToken(Long doctorId, LocalDate date);
    
    /**
     * Call the next token for a department
     * @param department the department
     * @param date the date
     * @return the called token
     */
    TokenDTO callNextTokenForDepartment(String department, LocalDate date);
    
    /**
     * Mark a token as completed
     * @param id the token ID
     * @return the updated token
     */
    TokenDTO completeToken(Long id);
    
    /**
     * Cancel a token
     * @param id the token ID
     * @return the updated token
     */
    TokenDTO cancelToken(Long id);
    
    /**
     * Generate a token number
     * @param tokenType the token type
     * @param department the department
     * @param date the date
     * @return the generated token number
     */
    String generateTokenNumber(Token.TokenType tokenType, String department, LocalDate date);
    
    /**
     * Count tokens for a department on a specific date
     * @param date the date
     * @param department the department
     * @return the count of tokens
     */
    long countTokensByDateAndDepartment(LocalDate date, String department);
    
    /**
     * Calculate average wait time in minutes for a department on a specific date
     * @param date the date
     * @param department the department
     * @return the average wait time in minutes
     */
    Double calculateAverageWaitTime(LocalDate date, String department);
}