package com.ingestion.patient.repository;

import com.ingestion.patient.model.Feedback;
import com.ingestion.patient.model.Feedback.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    List<Feedback> findByPatientId(Long patientId);
    
    List<Feedback> findByDoctorId(Long doctorId);
    
    @Query("SELECT f FROM Feedback f WHERE f.patient.id = :patientId ORDER BY f.feedbackDate DESC")
    List<Feedback> findByPatientIdOrderByDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT f FROM Feedback f WHERE f.doctor.id = :doctorId ORDER BY f.feedbackDate DESC")
    List<Feedback> findByDoctorIdOrderByDateDesc(@Param("doctorId") Long doctorId);
    
    @Query("SELECT f FROM Feedback f WHERE f.doctor.id = :doctorId AND f.isPublished = true ORDER BY f.feedbackDate DESC")
    List<Feedback> findPublishedByDoctorId(@Param("doctorId") Long doctorId);
    
    @Query("SELECT f FROM Feedback f WHERE f.feedbackType = :type ORDER BY f.feedbackDate DESC")
    List<Feedback> findByFeedbackType(@Param("type") FeedbackType type);
    
    @Query("SELECT f FROM Feedback f WHERE f.rating >= :minRating ORDER BY f.feedbackDate DESC")
    List<Feedback> findByRatingGreaterThanEqual(@Param("minRating") Integer minRating);
    
    @Query("SELECT f FROM Feedback f WHERE f.doctor.id = :doctorId AND f.rating >= :minRating ORDER BY f.feedbackDate DESC")
    List<Feedback> findByDoctorIdAndRatingGreaterThanEqual(
            @Param("doctorId") Long doctorId,
            @Param("minRating") Integer minRating);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.doctor.id = :doctorId AND f.isPublished = true")
    Double getAverageRatingByDoctorId(@Param("doctorId") Long doctorId);
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.doctor.id = :doctorId AND f.isPublished = true")
    Integer getTotalRatingsByDoctorId(@Param("doctorId") Long doctorId);
    
    @Query("SELECT f FROM Feedback f WHERE f.adminResponse IS NULL ORDER BY f.feedbackDate ASC")
    List<Feedback> findFeedbacksWithoutResponse();
}