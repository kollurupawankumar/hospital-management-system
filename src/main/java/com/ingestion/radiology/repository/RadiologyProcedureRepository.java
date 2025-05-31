package com.ingestion.radiology.repository;

import com.ingestion.radiology.model.RadiologyProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RadiologyProcedureRepository extends JpaRepository<RadiologyProcedure, Long> {

    Optional<RadiologyProcedure> findByProcedureCode(String procedureCode);
    
    List<RadiologyProcedure> findByProcedureNameContainingIgnoreCase(String procedureName);
    
    List<RadiologyProcedure> findByModality(RadiologyProcedure.Modality modality);
    
    List<RadiologyProcedure> findByBodyPartContainingIgnoreCase(String bodyPart);
    
    List<RadiologyProcedure> findByIsActive(Boolean isActive);
    
    List<RadiologyProcedure> findByContrastRequired(Boolean contrastRequired);
    
    @Query("SELECT p FROM RadiologyProcedure p WHERE p.isActive = true ORDER BY p.procedureName")
    List<RadiologyProcedure> findAllActiveProcedures();
    
    @Query("SELECT p FROM RadiologyProcedure p WHERE p.modality = :modality AND p.isActive = true ORDER BY p.procedureName")
    List<RadiologyProcedure> findActiveByModalityOrderByName(@Param("modality") RadiologyProcedure.Modality modality);
    
    @Query("SELECT DISTINCT p.modality FROM RadiologyProcedure p WHERE p.isActive = true ORDER BY p.modality")
    List<RadiologyProcedure.Modality> findAllActiveModalities();
    
    @Query("SELECT DISTINCT p.bodyPart FROM RadiologyProcedure p WHERE p.isActive = true AND p.bodyPart IS NOT NULL ORDER BY p.bodyPart")
    List<String> findAllActiveBodyParts();
}