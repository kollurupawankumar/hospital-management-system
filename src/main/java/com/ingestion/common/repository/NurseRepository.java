package com.ingestion.common.repository;

import com.ingestion.common.model.inpatient.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    
    Optional<Nurse> findByEmail(String email);
    
    Optional<Nurse> findByNurseId(String nurseId);
    
    Optional<Nurse> findByLicenseNumber(String licenseNumber);
    
    List<Nurse> findByDepartment(String department);
    
    List<Nurse> findByNurseType(Nurse.NurseType nurseType);
    
    List<Nurse> findByIsActiveTrue();
    
    @Query("SELECT n FROM Nurse n WHERE n.isActive = true AND n.department = ?1")
    List<Nurse> findActiveDepartmentNurses(String department);
    
    @Query("SELECT n FROM Nurse n WHERE LOWER(n.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(n.lastName) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Nurse> findByNameContaining(String name);
}