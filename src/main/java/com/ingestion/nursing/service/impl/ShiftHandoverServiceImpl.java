package com.ingestion.nursing.service.impl;

import com.ingestion.common.model.inpatient.Department;
import com.ingestion.nursing.model.ShiftHandover;
import com.ingestion.nursing.model.ShiftHandoverPatient;
import com.ingestion.nursing.repository.ShiftHandoverPatientRepository;
import com.ingestion.nursing.repository.ShiftHandoverRepository;
import com.ingestion.nursing.service.ShiftHandoverService;
import com.ingestion.patient.model.Patient;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShiftHandoverServiceImpl implements ShiftHandoverService {

    @Autowired
    private ShiftHandoverRepository shiftHandoverRepository;

    @Autowired
    private ShiftHandoverPatientRepository shiftHandoverPatientRepository;

    @Override
    public ShiftHandover saveShiftHandover(ShiftHandover shiftHandover) {
        return shiftHandoverRepository.save(shiftHandover);
    }

    @Override
    public Optional<ShiftHandover> findById(Long id) {
        return shiftHandoverRepository.findById(id);
    }

    @Override
    public List<ShiftHandover> findByOutgoingNurse(User nurse) {
        return shiftHandoverRepository.findByOutgoingNurseOrderByHandoverDateDesc(nurse);
    }

    @Override
    public List<ShiftHandover> findByIncomingNurse(User nurse) {
        return shiftHandoverRepository.findByIncomingNurseOrderByHandoverDateDesc(nurse);
    }

    @Override
    public Page<ShiftHandover> findByDepartment(Department department, Pageable pageable) {
        return shiftHandoverRepository.findByDepartmentOrderByHandoverDateDesc(department, pageable);
    }

    @Override
    public List<ShiftHandover> findByDepartmentAndDateRange(Department department, LocalDateTime startDate, LocalDateTime endDate) {
        return shiftHandoverRepository.findByDepartmentAndHandoverDateBetweenOrderByHandoverDateDesc(department, startDate, endDate);
    }

    @Override
    public List<ShiftHandover> findByShiftTypeAndDateRange(ShiftHandover.ShiftType shiftType, LocalDateTime startDate, LocalDateTime endDate) {
        return shiftHandoverRepository.findByShiftTypeAndHandoverDateBetweenOrderByHandoverDateDesc(shiftType, startDate, endDate);
    }

    @Override
    public List<ShiftHandover> findUnacknowledgedHandoversByNurse(User nurse) {
        return shiftHandoverRepository.findByIsAcknowledgedFalseAndIncomingNurseOrderByHandoverDateDesc(nurse);
    }

    @Override
    public List<ShiftHandover> findTodayHandovers() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return shiftHandoverRepository.findTodayHandovers(startOfDay, endOfDay);
    }

    @Override
    public List<ShiftHandover> findUnacknowledgedHandoversOlderThan(int hours) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(hours);
        return shiftHandoverRepository.findUnacknowledgedHandoversOlderThan(cutoffTime);
    }

    @Override
    public ShiftHandover createShiftHandover(LocalDateTime handoverDate, ShiftHandover.ShiftType shiftType,
                                           Department department, User outgoingNurse, User incomingNurse,
                                           String generalNotes, String pendingTasks, String criticalPatients,
                                           String medicationIssues, String equipmentIssues, String staffingIssues) {
        ShiftHandover handover = new ShiftHandover();
        handover.setHandoverDate(handoverDate);
        handover.setShiftType(shiftType);
        handover.setDepartment(department);
        handover.setOutgoingNurse(outgoingNurse);
        handover.setIncomingNurse(incomingNurse);
        handover.setGeneralNotes(generalNotes);
        handover.setPendingTasks(pendingTasks);
        handover.setCriticalPatients(criticalPatients);
        handover.setMedicationIssues(medicationIssues);
        handover.setEquipmentIssues(equipmentIssues);
        handover.setStaffingIssues(staffingIssues);
        handover.setIsAcknowledged(false);
        handover.setCreatedAt(LocalDateTime.now());
        
        return shiftHandoverRepository.save(handover);
    }

    @Override
    public ShiftHandoverPatient addPatientToHandover(Long handoverId, Patient patient, String currentStatus,
                                                   String carePlanUpdates, String medicationUpdates,
                                                   String vitalSignsUpdates, String pendingTasks,
                                                   String specialInstructions, Boolean isCritical) {
        Optional<ShiftHandover> handoverOpt = shiftHandoverRepository.findById(handoverId);
        if (handoverOpt.isPresent()) {
            ShiftHandover handover = handoverOpt.get();
            
            ShiftHandoverPatient handoverPatient = new ShiftHandoverPatient();
            handoverPatient.setShiftHandover(handover);
            handoverPatient.setPatient(patient);
            handoverPatient.setCurrentStatus(currentStatus);
            handoverPatient.setCarePlanUpdates(carePlanUpdates);
            handoverPatient.setMedicationUpdates(medicationUpdates);
            handoverPatient.setVitalSignsUpdates(vitalSignsUpdates);
            handoverPatient.setPendingTasks(pendingTasks);
            handoverPatient.setSpecialInstructions(specialInstructions);
            handoverPatient.setIsCritical(isCritical);
            handoverPatient.setCreatedAt(LocalDateTime.now());
            
            return shiftHandoverPatientRepository.save(handoverPatient);
        }
        throw new RuntimeException("Shift handover not found with id: " + handoverId);
    }

    @Override
    public ShiftHandover acknowledgeHandover(Long handoverId) {
        Optional<ShiftHandover> handoverOpt = shiftHandoverRepository.findById(handoverId);
        if (handoverOpt.isPresent()) {
            ShiftHandover handover = handoverOpt.get();
            handover.setIsAcknowledged(true);
            handover.setAcknowledgedAt(LocalDateTime.now());
            return shiftHandoverRepository.save(handover);
        }
        throw new RuntimeException("Shift handover not found with id: " + handoverId);
    }

    @Override
    public ShiftHandoverPatient findLatestHandoverForPatient(Patient patient) {
        return shiftHandoverPatientRepository.findLatestHandoverForPatient(patient);
    }

    @Override
    public List<ShiftHandoverPatient> findRecentCriticalPatients(int hoursBack) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(hoursBack);
        return shiftHandoverPatientRepository.findRecentCriticalPatients(cutoffTime);
    }

    @Override
    public void deleteShiftHandover(Long id) {
        shiftHandoverRepository.deleteById(id);
    }

    @Override
    public void removePatientFromHandover(Long handoverPatientId) {
        shiftHandoverPatientRepository.deleteById(handoverPatientId);
    }
}