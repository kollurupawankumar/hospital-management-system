package com.ingestion.reception.service.impl;

import com.ingestion.reception.dto.QueueManagementDTO;
import com.ingestion.reception.model.QueueManagement;
import com.ingestion.reception.repository.QueueManagementRepository;
import com.ingestion.reception.service.QueueManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueueManagementServiceImpl implements QueueManagementService {

    @Autowired
    private QueueManagementRepository queueManagementRepository;



    @Override
    @Transactional
    public QueueManagementDTO createQueue(QueueManagementDTO queueDTO) {
        QueueManagement queue = new QueueManagement();
        queue.setQueueName(queueDTO.getQueueName());
        queue.setDepartment(queueDTO.getDepartment());
        queue.setQueueDate(queueDTO.getQueueDate());
        queue.setStartTime(queueDTO.getStartTime() != null ? queueDTO.getStartTime() : LocalDateTime.now());
        queue.setEndTime(queueDTO.getEndTime());
        queue.setMaxCapacity(queueDTO.getMaxCapacity());
        queue.setCurrentCount(queueDTO.getCurrentCount());
        queue.setAverageWaitTimeMinutes(queueDTO.getAverageWaitTimeMinutes());
        queue.setCurrentTokenNumber(queueDTO.getCurrentTokenNumber());
        queue.setNextTokenNumber(queueDTO.getNextTokenNumber());
        queue.setQueueStatus(queueDTO.getQueueStatus());
        queue.setCounterNumbers(queueDTO.getCounterNumbers());
        queue.setManagedBy(queueDTO.getManagedBy());
        queue.setNotes(queueDTO.getNotes());
        
        QueueManagement savedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(savedQueue);
    }

    @Override
    public QueueManagementDTO getQueueById(Long id) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        return convertToDTO(queue);
    }

    @Override
    public List<QueueManagementDTO> getQueuesByDate(LocalDate date) {
        List<QueueManagement> queues = queueManagementRepository.findByQueueDate(date);
        
        return queues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QueueManagementDTO> getQueuesByDateAndDepartment(LocalDate date, String department) {
        List<QueueManagement> queues = queueManagementRepository.findByQueueDateAndDepartment(date, department);
        
        return queues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QueueManagementDTO getQueueByNameAndDate(String queueName, LocalDate date) {
        QueueManagement queue = queueManagementRepository.findByQueueNameAndQueueDate(queueName, date)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with name: " + queueName + " and date: " + date));
        
        return convertToDTO(queue);
    }

    @Override
    public List<QueueManagementDTO> getActiveQueuesByDate(LocalDate date) {
        List<QueueManagement> queues = queueManagementRepository.findActiveQueuesByDate(date);
        
        return queues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QueueManagementDTO> getActiveQueuesByDateAndDepartment(LocalDate date, String department) {
        List<QueueManagement> queues = queueManagementRepository.findActiveQueuesByDateAndDepartment(date, department);
        
        return queues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QueueManagementDTO> getQueuesWithAvailabilityByDate(LocalDate date) {
        List<QueueManagement> queues = queueManagementRepository.findQueuesWithAvailabilityByDate(date);
        
        return queues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QueueManagementDTO> getQueuesWithAvailabilityByDateAndDepartment(LocalDate date, String department) {
        List<QueueManagement> queues = queueManagementRepository.findQueuesWithAvailabilityByDateAndDepartment(date, department);
        
        return queues.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QueueManagementDTO updateQueue(Long id, QueueManagementDTO queueDTO) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        queue.setQueueName(queueDTO.getQueueName());
        queue.setDepartment(queueDTO.getDepartment());
        queue.setMaxCapacity(queueDTO.getMaxCapacity());
        queue.setCurrentCount(queueDTO.getCurrentCount());
        queue.setAverageWaitTimeMinutes(queueDTO.getAverageWaitTimeMinutes());
        queue.setCurrentTokenNumber(queueDTO.getCurrentTokenNumber());
        queue.setNextTokenNumber(queueDTO.getNextTokenNumber());
        queue.setQueueStatus(queueDTO.getQueueStatus());
        queue.setCounterNumbers(queueDTO.getCounterNumbers());
        queue.setManagedBy(queueDTO.getManagedBy());
        queue.setNotes(queueDTO.getNotes());
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    @Transactional
    public QueueManagementDTO updateQueueStatus(Long id, QueueManagement.QueueStatus status) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        queue.setQueueStatus(status);
        
        if (status == QueueManagement.QueueStatus.CLOSED) {
            queue.setEndTime(LocalDateTime.now());
        }
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    @Transactional
    public QueueManagementDTO incrementQueueCount(Long id) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        if (queue.getQueueStatus() != QueueManagement.QueueStatus.ACTIVE) {
            throw new IllegalStateException("Queue is not active");
        }
        
        if (queue.getCurrentCount() >= queue.getMaxCapacity()) {
            queue.setQueueStatus(QueueManagement.QueueStatus.FULL);
            throw new IllegalStateException("Queue is at maximum capacity");
        }
        
        queue.setCurrentCount(queue.getCurrentCount() + 1);
        
        if (queue.getCurrentCount() >= queue.getMaxCapacity()) {
            queue.setQueueStatus(QueueManagement.QueueStatus.FULL);
        }
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    @Transactional
    public QueueManagementDTO decrementQueueCount(Long id) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        if (queue.getCurrentCount() <= 0) {
            throw new IllegalStateException("Queue count is already at minimum");
        }
        
        queue.setCurrentCount(queue.getCurrentCount() - 1);
        
        if (queue.getQueueStatus() == QueueManagement.QueueStatus.FULL && queue.getCurrentCount() < queue.getMaxCapacity()) {
            queue.setQueueStatus(QueueManagement.QueueStatus.ACTIVE);
        }
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    @Transactional
    public QueueManagementDTO updateCurrentToken(Long id, String tokenNumber) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        queue.setCurrentTokenNumber(tokenNumber);
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    @Transactional
    public QueueManagementDTO updateNextToken(Long id, String tokenNumber) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        queue.setNextTokenNumber(tokenNumber);
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    @Transactional
    public QueueManagementDTO updateAverageWaitTime(Long id, Integer averageWaitTimeMinutes) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        queue.setAverageWaitTimeMinutes(averageWaitTimeMinutes);
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    @Transactional
    public QueueManagementDTO closeQueue(Long id) {
        QueueManagement queue = queueManagementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Queue not found with ID: " + id));
        
        queue.setQueueStatus(QueueManagement.QueueStatus.CLOSED);
        queue.setEndTime(LocalDateTime.now());
        
        QueueManagement updatedQueue = queueManagementRepository.save(queue);
        
        return convertToDTO(updatedQueue);
    }

    @Override
    public Double calculateAverageWaitTimeForDepartment(LocalDate date, String department) {
        return queueManagementRepository.calculateAverageWaitTimeForDepartment(date, department);
    }
    
    private QueueManagementDTO convertToDTO(QueueManagement queue) {
        QueueManagementDTO dto = new QueueManagementDTO();
        dto.setId(queue.getId());
        dto.setQueueName(queue.getQueueName());
        dto.setDepartment(queue.getDepartment());
        dto.setQueueDate(queue.getQueueDate());
        dto.setStartTime(queue.getStartTime());
        dto.setEndTime(queue.getEndTime());
        dto.setMaxCapacity(queue.getMaxCapacity());
        dto.setCurrentCount(queue.getCurrentCount());
        dto.setAverageWaitTimeMinutes(queue.getAverageWaitTimeMinutes());
        dto.setCurrentTokenNumber(queue.getCurrentTokenNumber());
        dto.setNextTokenNumber(queue.getNextTokenNumber());
        dto.setQueueStatus(queue.getQueueStatus());
        dto.setCounterNumbers(queue.getCounterNumbers());
        dto.setManagedBy(queue.getManagedBy());
        dto.setNotes(queue.getNotes());
        
        // Format status for display
        switch (queue.getQueueStatus()) {
            case ACTIVE:
                dto.setStatusDisplay("Active");
                break;
            case PAUSED:
                dto.setStatusDisplay("Paused");
                break;
            case CLOSED:
                dto.setStatusDisplay("Closed");
                break;
            case FULL:
                dto.setStatusDisplay("Full");
                break;
            default:
                dto.setStatusDisplay(queue.getQueueStatus().toString());
        }
        
        // Format capacity display
        dto.setCapacityDisplay(queue.getCurrentCount() + "/" + queue.getMaxCapacity());
        
        // Check if queue has capacity
        dto.setHasCapacity(queue.hasCapacity());
        
        // Format estimated wait time
        if (queue.getAverageWaitTimeMinutes() != null) {
            int waitTime = queue.getAverageWaitTimeMinutes() * queue.getCurrentCount();
            if (waitTime < 60) {
                dto.setEstimatedWaitTime(waitTime + " minutes");
            } else {
                int hours = waitTime / 60;
                int minutes = waitTime % 60;
                dto.setEstimatedWaitTime(hours + " hour" + (hours > 1 ? "s" : "") + 
                        (minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : ""));
            }
        }
        
        return dto;
    }
}