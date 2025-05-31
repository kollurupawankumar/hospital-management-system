package com.ingestion.doctor.service;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.doctor.repository.DoctorRepository;
import com.ingestion.patient.repository.FeedbackRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    
    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Transactional(readOnly = true)
    public List<Doctor> getAllDoctors() {
        log.debug("Fetching all doctors");
        return doctorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Doctor> getAllActiveDoctors() {
        log.debug("Fetching all active doctors");
        return doctorRepository.findAllActiveDoctors();
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getDoctorById(Long id) {
        log.debug("Fetching doctor with ID: {}", id);
        return doctorRepository.findById(id);
    }

    @Transactional
    public Doctor saveDoctor(Doctor doctor) {
        log.debug("Saving doctor: {}", doctor);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        log.debug("Deleting doctor with ID: {}", id);
        doctorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        log.debug("Fetching doctors with specialization: {}", specialization);
        return doctorRepository.findBySpecialization(specialization);
    }
    
    @Transactional(readOnly = true)
    public List<Doctor> getActiveDoctorsBySpecialization(String specialization) {
        log.debug("Fetching active doctors with specialization: {}", specialization);
        return doctorRepository.findActiveBySpecialization(specialization);
    }
    
    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsByDepartment(String department) {
        log.debug("Fetching doctors by department: {}", department);
        return doctorRepository.findByDepartment(department);
    }
    
    @Transactional(readOnly = true)
    public List<Doctor> getActiveDoctorsByDepartment(String department) {
        log.debug("Fetching active doctors by department: {}", department);
        return doctorRepository.findActiveByDepartment(department);
    }

    @Transactional(readOnly = true)
    public List<Doctor> searchDoctors(String searchTerm) {
        log.debug("Searching doctors with term: {}", searchTerm);
        return doctorRepository.searchDoctors(searchTerm);
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsAvailableForTelemedicine() {
        log.debug("Fetching doctors available for telemedicine");
        return doctorRepository.findDoctorsAvailableForTelemedicine();
    }

    @Transactional(readOnly = true)
    public List<Doctor> getTopRatedDoctors() {
        log.debug("Fetching top rated doctors");
        return doctorRepository.findAllOrderByRatingDesc();
    }

    @Transactional(readOnly = true)
    public List<Doctor> getTopRatedDoctorsBySpecialization(String specialization) {
        log.debug("Fetching top rated doctors with specialization: {}", specialization);
        return doctorRepository.findBySpecializationOrderByRatingDesc(specialization);
    }

    @Transactional(readOnly = true)
    public List<String> getAllSpecializations() {
        log.debug("Fetching all specializations");
        return doctorRepository.findAllSpecializations();
    }

    @Transactional
    public void updateDoctorRating(Long doctorId) {
        log.debug("Updating rating for doctor ID: {}", doctorId);
        
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            
            Double averageRating = feedbackRepository.getAverageRatingByDoctorId(doctorId);
            Integer totalRatings = feedbackRepository.getTotalRatingsByDoctorId(doctorId);
            
            if (averageRating != null && totalRatings != null) {
                doctor.setAverageRatings(averageRating);
                doctor.setTotalRatings(totalRatings);
                doctorRepository.save(doctor);
            }
        }
    }

    @Transactional
    public void toggleDoctorActiveStatus(Long doctorId, boolean active) {
        log.debug("Toggling active status for doctor ID: {} to {}", doctorId, active);
        
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.setActive(active);
            doctorRepository.save(doctor);
        } else {
            log.error("Doctor ID: {} not found", doctorId);
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Transactional
    public void toggleTelemedicineAvailability(Long doctorId, boolean available) {
        log.debug("Toggling telemedicine availability for doctor ID: {} to {}", doctorId, available);
        
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.setAvailableForTelemedicine(available);
            doctorRepository.save(doctor);
        } else {
            log.error("Doctor ID: {} not found", doctorId);
            throw new IllegalArgumentException("Doctor not found");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsByExperience(Integer minYears) {
        log.debug("Fetching doctors with minimum experience of {} years", minYears);
        return doctorRepository.findByExperienceYearsGreaterThanEqual(minYears);
    }
    
    @Transactional(readOnly = true)
    public List<Object> getPendingTasksForDoctor(Long doctorId) {
        log.debug("Fetching pending tasks for doctor ID: {}", doctorId);
        // This is a placeholder implementation
        // In a real application, you would fetch tasks from a task repository
        return List.of(); // Return empty list for now
    }
    
    @Transactional(readOnly = true)
    public Object getDutyRosterForMonth(Long doctorId, LocalDate month) {
        log.debug("Fetching duty roster for doctor ID: {} for month: {}", doctorId, month);
        // This is a placeholder implementation
        // In a real application, you would fetch the duty roster from a repository
        return new Object(); // Return empty object for now
    }
    
    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        log.debug("Finding all doctors");
        return doctorRepository.findAll();
    }
}