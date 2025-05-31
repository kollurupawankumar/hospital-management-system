package com.ingestion.patient.repository;

import com.ingestion.patient.model.Notification;
import com.ingestion.patient.model.Notification.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByPatientId(Long patientId);
    
    @Query("SELECT n FROM Notification n WHERE n.patient.id = :patientId ORDER BY n.notificationDate DESC")
    List<Notification> findByPatientIdOrderByDateDesc(@Param("patientId") Long patientId);
    
    @Query("SELECT n FROM Notification n WHERE n.patient.id = :patientId AND n.isRead = false ORDER BY n.notificationDate DESC")
    List<Notification> findUnreadByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT n FROM Notification n WHERE n.patient.id = :patientId AND n.notificationType = :type ORDER BY n.notificationDate DESC")
    List<Notification> findByPatientIdAndType(
            @Param("patientId") Long patientId,
            @Param("type") NotificationType type);
    
    @Query("SELECT n FROM Notification n WHERE n.isSent = false ORDER BY n.notificationDate ASC")
    List<Notification> findUnsentNotifications();
    
    @Query("SELECT n FROM Notification n WHERE n.notificationType = :type AND n.isSent = false ORDER BY n.notificationDate ASC")
    List<Notification> findUnsentNotificationsByType(@Param("type") NotificationType type);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.patient.id = :patientId AND n.isRead = false")
    Integer countUnreadByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT n FROM Notification n WHERE n.relatedEntityType = :entityType AND n.relatedEntityId = :entityId")
    List<Notification> findByRelatedEntity(
            @Param("entityType") String entityType,
            @Param("entityId") Long entityId);
    
    @Query("SELECT n FROM Notification n WHERE n.notificationDate < :date AND n.isRead = true")
    List<Notification> findOldReadNotifications(@Param("date") LocalDateTime date);
}