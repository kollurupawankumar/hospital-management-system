package com.ingestion.common.laboratory;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.laboratory.model.LabTest;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.patient.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the consolidated LabOrderService.
 */
@Service("commonLabOrderService")
public class LabOrderServiceImpl implements LabOrderService {

    @Autowired
    private LabOrderRepository labOrderRepository;
    
    @Autowired
    private LabOrderTestRepository labOrderTestRepository;

    @Override
    public List<LabOrder> findAll() {
        return labOrderRepository.findAll();
    }

    @Override
    public Optional<LabOrder> findById(Long id) {
        return labOrderRepository.findById(id);
    }

    @Override
    public Optional<LabOrder> findByOrderNumber(String orderNumber) {
        return labOrderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public LabOrder save(LabOrder labOrder) {
        return labOrderRepository.save(labOrder);
    }

    @Override
    public void deleteById(Long id) {
        labOrderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public LabOrder createOrder(
            Patient patient,
            Doctor doctor,
            OpdVisit opdVisit,
            LabOrder.OrderPriority priority,
            String clinicalNotes,
            String specialInstructions,
            List<LabTest> tests) {
        
        LabOrder order = new LabOrder();
        order.setPatient(patient);
        order.setDoctor(doctor);
        order.setOpdVisit(opdVisit);
        order.setOrderDate(LocalDateTime.now());
        order.setPriority(priority);
        order.setClinicalNotes(clinicalNotes);
        order.setSpecialInstructions(specialInstructions);
        order.setStatus(LabOrder.OrderStatus.ORDERED);
        
        // Save the order first to get an ID
        LabOrder savedOrder = labOrderRepository.save(order);
        
        // Add tests if provided
        if (tests != null && !tests.isEmpty()) {
            for (LabTest test : tests) {
                LabOrderTest orderTest = new LabOrderTest();
                orderTest.setLabOrder(savedOrder);
                orderTest.setLabTest(test);
                orderTest.setTestPrice(test.getPrice());
                orderTest.setReferenceRange(test.getReferenceRange());
                savedOrder.addLabTest(orderTest);
            }
            // Save again with the tests
            savedOrder = labOrderRepository.save(savedOrder);
        }
        
        return savedOrder;
    }

    @Override
    @Transactional
    public LabOrderTest addTestToOrder(Long orderId, LabTest test) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            
            // Check if the test is already in the order
            for (LabOrderTest existingTest : order.getLabTests()) {
                if (existingTest.getLabTest().getId().equals(test.getId())) {
                    return existingTest; // Test already exists
                }
            }
            
            // Add the new test
            LabOrderTest orderTest = new LabOrderTest();
            orderTest.setLabOrder(order);
            orderTest.setLabTest(test);
            orderTest.setTestPrice(test.getPrice());
            orderTest.setReferenceRange(test.getReferenceRange());
            
            order.addLabTest(orderTest);
            labOrderRepository.save(order);
            
            return orderTest;
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public void removeTestFromOrder(Long orderId, Long testId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            
            // Find and remove the test
            LabOrderTest testToRemove = null;
            for (LabOrderTest test : order.getLabTests()) {
                if (test.getId().equals(testId)) {
                    testToRemove = test;
                    break;
                }
            }
            
            if (testToRemove != null) {
                order.removeLabTest(testToRemove);
                labOrderRepository.save(order);
                labOrderTestRepository.deleteById(testId);
            } else {
                throw new IllegalArgumentException("Test not found in order with ID: " + testId);
            }
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public LabOrder updateOrderStatus(Long orderId, LabOrder.OrderStatus status) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.setStatus(status);
            
            // Update timestamps based on status
            if (status == LabOrder.OrderStatus.SAMPLE_COLLECTED) {
                order.setSampleCollectionDate(LocalDateTime.now());
            } else if (status == LabOrder.OrderStatus.COMPLETED) {
                order.setResultDate(LocalDateTime.now());
            }
            
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public LabOrder markAsSampleCollected(Long orderId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.markAsSampleCollected();
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public LabOrder markAsInProcess(Long orderId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.markAsInProcess();
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public LabOrder markAsCompleted(Long orderId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.markAsCompleted();
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public LabOrder cancelOrder(Long orderId, Doctor cancelledBy, String reason) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.markAsCancelled(cancelledBy, reason);
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }
    
    @Override
    @Transactional
    public LabOrder markAsCancelled(Long orderId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.setStatus(LabOrder.OrderStatus.CANCELLED);
            order.setCancellationDate(LocalDateTime.now());
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }
    
    @Override
    @Transactional
    public LabOrder markAsRejected(Long orderId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.setStatus(LabOrder.OrderStatus.REJECTED);
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }
    
    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        if (labOrderRepository.existsById(orderId)) {
            labOrderRepository.deleteById(orderId);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public LabOrder markAsBilled(Long orderId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.markAsBilled();
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    @Transactional
    public LabOrder markAsPaid(Long orderId) {
        Optional<LabOrder> orderOpt = labOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            order.markAsPaid();
            return labOrderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Lab order not found with ID: " + orderId);
        }
    }

    @Override
    public List<LabOrder> findByPatient(Patient patient) {
        return labOrderRepository.findByPatient(patient);
    }

    @Override
    public List<LabOrder> findByPatientId(Long patientId) {
        return labOrderRepository.findByPatientIdOrderByOrderDateDesc(patientId);
    }

    @Override
    public List<LabOrder> findByDoctor(Doctor doctor) {
        return labOrderRepository.findByDoctor(doctor);
    }

    @Override
    public List<LabOrder> findByDoctorId(Long doctorId) {
        return labOrderRepository.findByDoctorIdOrderByOrderDateDesc(doctorId);
    }

    @Override
    public List<LabOrder> findByOpdVisit(OpdVisit opdVisit) {
        return labOrderRepository.findByOpdVisit(opdVisit);
    }

    @Override
    public List<LabOrder> findByStatus(LabOrder.OrderStatus status) {
        return labOrderRepository.findByStatus(status);
    }

    @Override
    public List<LabOrder> findByPriority(LabOrder.OrderPriority priority) {
        return labOrderRepository.findByPriority(priority);
    }

    @Override
    public List<LabOrder> findByOrderDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return labOrderRepository.findByOrderDateBetween(startOfDay, endOfDay);
    }

    @Override
    public List<LabOrder> findByOrderDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return labOrderRepository.findByOrderDateBetween(startDate, endDate);
    }

    @Override
    public List<LabOrder> findByStatusesOrderByPriorityAndOrderDate(List<LabOrder.OrderStatus> statuses) {
        return labOrderRepository.findByStatusesOrderByPriorityAndOrderDate(statuses);
    }

    @Override
    public Page<LabOrder> findByPatientPaginated(Patient patient, Pageable pageable) {
        return labOrderRepository.findByPatientOrderByOrderDateDesc(patient, pageable);
    }

    @Override
    public List<LabOrder> findByTestName(String testName) {
        return labOrderRepository.findByTestName(testName);
    }

    @Override
    public Long countByStatus(LabOrder.OrderStatus status) {
        return labOrderRepository.countByStatus(status);
    }

    @Override
    public Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return labOrderRepository.countByDateRange(startDate, endDate);
    }
}