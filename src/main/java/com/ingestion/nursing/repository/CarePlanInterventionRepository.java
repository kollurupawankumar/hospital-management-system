package com.ingestion.nursing.repository;

import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.CarePlanGoal;
import com.ingestion.nursing.model.CarePlanIntervention;
import com.ingestion.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarePlanInterventionRepository extends JpaRepository<CarePlanIntervention, Long> {
    
    List<CarePlanIntervention> findByCarePlanOrderByStartDate(CarePlan carePlan);
    
    List<CarePlanIntervention> findByCarePlanAndStatusOrderByStartDate(
            CarePlan carePlan, CarePlanIntervention.InterventionStatus status);
    
    List<CarePlanIntervention> findByGoalOrderByStartDate(CarePlanGoal goal);
    
    List<CarePlanIntervention> findByCreatedByOrderByCreatedDateDesc(User nurse);
    
    List<CarePlanIntervention> findByLastUpdatedByOrderByLastUpdatedDateDesc(User nurse);
    
    @Query("SELECT i FROM CarePlanIntervention i WHERE i.status = 'ACTIVE' AND i.carePlan.status = 'ACTIVE' ORDER BY i.startDate ASC")
    List<CarePlanIntervention> findActiveInterventionsForActiveCarePlans();
    
    @Query("SELECT i FROM CarePlanIntervention i WHERE i.status = 'ACTIVE' AND i.carePlan = :carePlan ORDER BY i.startDate ASC")
    List<CarePlanIntervention> findActiveInterventionsForCarePlan(@Param("carePlan") CarePlan carePlan);
    
    @Query("SELECT i FROM CarePlanIntervention i WHERE i.status = 'ACTIVE' AND i.goal = :goal ORDER BY i.startDate ASC")
    List<CarePlanIntervention> findActiveInterventionsForGoal(@Param("goal") CarePlanGoal goal);
}