package com.ingestion.reception.service;

import com.ingestion.reception.dto.QueueManagementDTO;
import com.ingestion.reception.model.QueueManagement;

import java.time.LocalDate;
import java.util.List;

public interface QueueManagementService {
    
    /**
     * Create a new queue
     * @param queueDTO the queue data
     * @return the created queue
     */
    QueueManagementDTO createQueue(QueueManagementDTO queueDTO);
    
    /**
     * Get a queue by ID
     * @param id the queue ID
     * @return the queue
     */
    QueueManagementDTO getQueueById(Long id);
    
    /**
     * Get queues for a specific date
     * @param date the date
     * @return list of queues
     */
    List<QueueManagementDTO> getQueuesByDate(LocalDate date);
    
    /**
     * Get queues for a specific date and department
     * @param date the date
     * @param department the department
     * @return list of queues
     */
    List<QueueManagementDTO> getQueuesByDateAndDepartment(LocalDate date, String department);
    
    /**
     * Get a queue by name and date
     * @param queueName the queue name
     * @param date the date
     * @return the queue
     */
    QueueManagementDTO getQueueByNameAndDate(String queueName, LocalDate date);
    
    /**
     * Get active queues for a specific date
     * @param date the date
     * @return list of active queues
     */
    List<QueueManagementDTO> getActiveQueuesByDate(LocalDate date);
    
    /**
     * Get active queues for a specific date and department
     * @param date the date
     * @param department the department
     * @return list of active queues
     */
    List<QueueManagementDTO> getActiveQueuesByDateAndDepartment(LocalDate date, String department);
    
    /**
     * Get queues with availability for a specific date
     * @param date the date
     * @return list of queues with availability
     */
    List<QueueManagementDTO> getQueuesWithAvailabilityByDate(LocalDate date);
    
    /**
     * Get queues with availability for a specific date and department
     * @param date the date
     * @param department the department
     * @return list of queues with availability
     */
    List<QueueManagementDTO> getQueuesWithAvailabilityByDateAndDepartment(LocalDate date, String department);
    
    /**
     * Update a queue
     * @param id the queue ID
     * @param queueDTO the updated queue data
     * @return the updated queue
     */
    QueueManagementDTO updateQueue(Long id, QueueManagementDTO queueDTO);
    
    /**
     * Update queue status
     * @param id the queue ID
     * @param status the new status
     * @return the updated queue
     */
    QueueManagementDTO updateQueueStatus(Long id, QueueManagement.QueueStatus status);
    
    /**
     * Increment queue count
     * @param id the queue ID
     * @return the updated queue
     */
    QueueManagementDTO incrementQueueCount(Long id);
    
    /**
     * Decrement queue count
     * @param id the queue ID
     * @return the updated queue
     */
    QueueManagementDTO decrementQueueCount(Long id);
    
    /**
     * Update current token number
     * @param id the queue ID
     * @param tokenNumber the current token number
     * @return the updated queue
     */
    QueueManagementDTO updateCurrentToken(Long id, String tokenNumber);
    
    /**
     * Update next token number
     * @param id the queue ID
     * @param tokenNumber the next token number
     * @return the updated queue
     */
    QueueManagementDTO updateNextToken(Long id, String tokenNumber);
    
    /**
     * Update average wait time
     * @param id the queue ID
     * @param averageWaitTimeMinutes the average wait time in minutes
     * @return the updated queue
     */
    QueueManagementDTO updateAverageWaitTime(Long id, Integer averageWaitTimeMinutes);
    
    /**
     * Close a queue
     * @param id the queue ID
     * @return the updated queue
     */
    QueueManagementDTO closeQueue(Long id);
    
    /**
     * Calculate average wait time for a department
     * @param date the date
     * @param department the department
     * @return the average wait time in minutes
     */
    Double calculateAverageWaitTimeForDepartment(LocalDate date, String department);
}