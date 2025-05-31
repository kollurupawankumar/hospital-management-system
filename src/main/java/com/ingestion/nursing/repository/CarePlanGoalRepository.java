package com.ingestion.nursing.repository;

import com.ingestion.nursing.model.CarePlan;
import com.ingestion.nursing.model.CarePlanGoal;
import com.ingestion.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarePlanGoalRepository extends JpaRepository<CarePlanGoal, Long> {
    
    List<CarePlanGoal> findByCarePlanOrderByTargetDate(CarePlan carePlan);
    
    List<CarePlanGoal> findByCarePlanAndStatusOrderByTargetDate(
            CarePlan carePlan, CarePlanGoal.GoalStatus status);
    
    List<CarePlanGoal> findByCarePlanAndPriorityOrderByTargetDate(
            CarePlan carePlan, CarePlanGoal.GoalPriority priority);
    
    List<CarePlanGoal> findByCompletedByOrderByCompletionDateDesc(User nurse);
    
    @Query("SELECT g FROM CarePlanGoal g WHERE g.status = 'IN_PROGRESS' AND g.targetDate < :now ORDER BY g.targetDate ASC")
    List<CarePlanGoal> findOverdueGoals(@Param("now") LocalDateTime now);
    
    @Query("SELECT g FROM CarePlanGoal g WHERE g.status = 'IN_PROGRESS' AND g.targetDate BETWEEN :start AND :end ORDER BY g.targetDate ASC")
    List<CarePlanGoal> findUpcomingGoals(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT g FROM CarePlanGoal g WHERE g.status = 'IN_PROGRESS' AND g.carePlan.status = 'ACTIVE' AND g.priority = 'HIGH' ORDER BY g.targetDate ASC")
    List<CarePlanGoal> findHighPriorityActiveGoals();
}