package com.ingestion.opd.service.impl;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.opd.repository.OpdVisitRepository;
import com.ingestion.opd.service.OpdVisitService;
import com.ingestion.patient.model.Appointment;
import com.ingestion.patient.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OpdVisitServiceImpl implements OpdVisitService {

    @Autowired
    private OpdVisitRepository opdVisitRepository;

    @Override
    @Transactional
    public OpdVisit saveVisit(OpdVisit opdVisit) {
        return opdVisitRepository.save(opdVisit);
    }

    @Override
    public Optional<OpdVisit> findById(Long id) {
        return opdVisitRepository.findById(id);
    }

    @Override
    public Optional<OpdVisit> findByTokenNumber(String tokenNumber) {
        return opdVisitRepository.findByTokenNumber(tokenNumber);
    }

    @Override
    public List<OpdVisit> findAll() {
        return opdVisitRepository.findAll();
    }

    @Override
    public Page<OpdVisit> findAll(Pageable pageable) {
        return opdVisitRepository.findAll(pageable);
    }

    @Override
    public List<OpdVisit> findByPatient(Patient patient) {
        return opdVisitRepository.findByPatient(patient);
    }

    @Override
    public List<OpdVisit> findByDoctor(Doctor doctor) {
        return opdVisitRepository.findByDoctor(doctor);
    }

    @Override
    public List<OpdVisit> findByVisitDate(LocalDate visitDate) {
        LocalDateTime startOfDay = visitDate.atStartOfDay();
        LocalDateTime endOfDay = visitDate.atTime(LocalTime.MAX);
        return opdVisitRepository.findByVisitDateBetween(startOfDay, endOfDay);
    }

    @Override
    public List<OpdVisit> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return opdVisitRepository.findByVisitDateBetween(startDate, endDate);
    }

    @Override
    public List<OpdVisit> findByStatus(OpdVisit.VisitStatus status) {
        return opdVisitRepository.findByVisitStatus(status);
    }

    @Override
    public List<OpdVisit> findTodayVisitsByDoctor(Doctor doctor) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<OpdVisit.VisitStatus> activeStatuses = Arrays.asList(
                OpdVisit.VisitStatus.REGISTERED,
                OpdVisit.VisitStatus.CHECKED_IN,
                OpdVisit.VisitStatus.WAITING,
                OpdVisit.VisitStatus.IN_CONSULTATION
        );
        return opdVisitRepository.findTodayVisitsByDoctor(doctor, today, activeStatuses);
    }

    @Override
    public Page<OpdVisit> findVisitsByDateRangeAndStatus(
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<OpdVisit.VisitStatus> statuses,
            Pageable pageable) {
        return opdVisitRepository.findVisitsByDateRangeAndStatus(startDate, endDate, statuses, pageable);
    }

    @Override
    public Page<OpdVisit> findVisitsByDoctorAndDateRangeAndStatus(
            Doctor doctor,
            LocalDateTime startDate,
            LocalDateTime endDate,
            List<OpdVisit.VisitStatus> statuses,
            Pageable pageable) {
        return opdVisitRepository.findVisitsByDoctorAndDateRangeAndStatus(doctor, startDate, endDate, statuses, pageable);
    }

    @Override
    public Long countVisitsByDoctorAndDate(Doctor doctor, LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        List<OpdVisit.VisitStatus> countableStatuses = Arrays.asList(
                OpdVisit.VisitStatus.REGISTERED,
                OpdVisit.VisitStatus.CHECKED_IN,
                OpdVisit.VisitStatus.WAITING,
                OpdVisit.VisitStatus.IN_CONSULTATION,
                OpdVisit.VisitStatus.COMPLETED
        );
        return opdVisitRepository.countVisitsByDoctorAndDate(doctor, dateTime, countableStatuses);
    }

    @Override
    public Page<OpdVisit> findPatientVisitHistory(Patient patient, Pageable pageable) {
        return opdVisitRepository.findPatientVisitHistory(patient, pageable);
    }

    @Override
    @Transactional
    public OpdVisit createVisitFromAppointment(Appointment appointment) {
        OpdVisit opdVisit = new OpdVisit();
        opdVisit.setPatient(appointment.getPatient());
        opdVisit.setDoctor(appointment.getDoctor());
        opdVisit.setAppointment(appointment);
        opdVisit.setVisitDate(appointment.getAppointmentDate());
        opdVisit.setChiefComplaint(appointment.getPurpose());
        opdVisit.setVisitStatus(OpdVisit.VisitStatus.REGISTERED);
        
        // Update appointment status
        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        
        return opdVisitRepository.save(opdVisit);
    }

    @Override
    @Transactional
    public OpdVisit createWalkInVisit(Patient patient, Doctor doctor, String chiefComplaint) {
        OpdVisit opdVisit = new OpdVisit();
        opdVisit.setPatient(patient);
        opdVisit.setDoctor(doctor);
        opdVisit.setVisitDate(LocalDateTime.now());
        opdVisit.setChiefComplaint(chiefComplaint);
        opdVisit.setVisitStatus(OpdVisit.VisitStatus.REGISTERED);
        
        return opdVisitRepository.save(opdVisit);
    }

    @Override
    @Transactional
    public OpdVisit checkInVisit(Long visitId) {
        Optional<OpdVisit> optionalVisit = opdVisitRepository.findById(visitId);
        if (optionalVisit.isPresent()) {
            OpdVisit visit = optionalVisit.get();
            visit.checkIn();
            return opdVisitRepository.save(visit);
        }
        throw new RuntimeException("Visit not found with id: " + visitId);
    }

    @Override
    @Transactional
    public OpdVisit startConsultation(Long visitId) {
        Optional<OpdVisit> optionalVisit = opdVisitRepository.findById(visitId);
        if (optionalVisit.isPresent()) {
            OpdVisit visit = optionalVisit.get();
            visit.startConsultation();
            return opdVisitRepository.save(visit);
        }
        throw new RuntimeException("Visit not found with id: " + visitId);
    }

    @Override
    @Transactional
    public OpdVisit completeConsultation(Long visitId, String diagnosis, String treatmentPlan, String followUpInstructions, LocalDateTime followUpDate) {
        Optional<OpdVisit> optionalVisit = opdVisitRepository.findById(visitId);
        if (optionalVisit.isPresent()) {
            OpdVisit visit = optionalVisit.get();
            visit.setDiagnosis(diagnosis);
            visit.setTreatmentPlan(treatmentPlan);
            visit.setFollowUpInstructions(followUpInstructions);
            visit.setFollowUpDate(followUpDate);
            visit.completeConsultation();
            
            // If there was an appointment, mark it as completed
            if (visit.getAppointment() != null) {
                visit.getAppointment().setStatus(Appointment.AppointmentStatus.COMPLETED);
            }
            
            return opdVisitRepository.save(visit);
        }
        throw new RuntimeException("Visit not found with id: " + visitId);
    }

    @Override
    @Transactional
    public OpdVisit cancelVisit(Long visitId) {
        Optional<OpdVisit> optionalVisit = opdVisitRepository.findById(visitId);
        if (optionalVisit.isPresent()) {
            OpdVisit visit = optionalVisit.get();
            visit.cancel();
            
            // If there was an appointment, mark it as cancelled
            if (visit.getAppointment() != null) {
                visit.getAppointment().setStatus(Appointment.AppointmentStatus.CANCELLED);
            }
            
            return opdVisitRepository.save(visit);
        }
        throw new RuntimeException("Visit not found with id: " + visitId);
    }

    @Override
    @Transactional
    public OpdVisit markNoShow(Long visitId) {
        Optional<OpdVisit> optionalVisit = opdVisitRepository.findById(visitId);
        if (optionalVisit.isPresent()) {
            OpdVisit visit = optionalVisit.get();
            visit.markNoShow();
            
            // If there was an appointment, mark it as no-show
            if (visit.getAppointment() != null) {
                visit.getAppointment().setStatus(Appointment.AppointmentStatus.NO_SHOW);
            }
            
            return opdVisitRepository.save(visit);
        }
        throw new RuntimeException("Visit not found with id: " + visitId);
    }

    @Override
    @Transactional
    public void deleteVisit(Long id) {
        opdVisitRepository.deleteById(id);
    }
}