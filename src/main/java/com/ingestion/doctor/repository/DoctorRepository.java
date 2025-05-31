package com.ingestion.doctor.repository;

import com.ingestion.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
    
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    
    List<Doctor> findBySpecialization(String specialization);
    
    List<Doctor> findByDepartment(String department);
    
    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    @Query("SELECT d FROM Doctor d WHERE d.isActive = true")
    List<Doctor> findAllActiveDoctors();
    
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization AND d.isActive = true")
    List<Doctor> findActiveBySpecialization(@Param("specialization") String specialization);
    
    @Query("SELECT d FROM Doctor d WHERE d.department = :department AND d.isActive = true")
    List<Doctor> findActiveByDepartment(@Param("department") String department);
    
    @Query("SELECT d FROM Doctor d WHERE d.experienceYears >= :years AND d.isActive = true")
    List<Doctor> findByExperienceYearsGreaterThanEqual(@Param("years") Integer years);
    
    @Query("SELECT d FROM Doctor d WHERE d.availableForTelemedicine = true")
    List<Doctor> findDoctorsAvailableForTelemedicine();
    
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Doctor> searchDoctors(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT d FROM Doctor d ORDER BY d.averageRatings DESC")
    List<Doctor> findAllOrderByRatingDesc();
    
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization ORDER BY d.averageRatings DESC")
    List<Doctor> findBySpecializationOrderByRatingDesc(@Param("specialization") String specialization);
    
    @Query("SELECT DISTINCT d.specialization FROM Doctor d ORDER BY d.specialization")
    List<String> findAllSpecializations();
}