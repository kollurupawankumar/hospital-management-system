package com.ingestion.common.service;

import com.ingestion.patient.model.Appointment;
import com.ingestion.patient.model.LabReport;
import com.ingestion.pharmacy.model.Prescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Send an email notification
     * @param to recipient email
     * @param subject email subject
     * @param body email body
     */
    public void sendEmailNotification(String to, String subject, String body) {
        // Implementation would connect to an email service
        log.info("Sending email to: {}", to);
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
    }
    
    /**
     * Send an SMS notification
     * @param phoneNumber recipient phone number
     * @param message SMS message
     */
    public void sendSmsNotification(String phoneNumber, String message) {
        // Implementation would connect to an SMS gateway
        log.info("Sending SMS to: {}", phoneNumber);
        log.info("Message: {}", message);
    }
    
    /**
     * Send an in-app notification
     * @param userId recipient user ID
     * @param title notification title
     * @param message notification message
     * @param type notification type
     */
    public void sendInAppNotification(Long userId, String title, String message, String type) {
        // Implementation would store notification in database
        log.info("Sending in-app notification to user: {}", userId);
        log.info("Title: {}", title);
        log.info("Message: {}", message);
        log.info("Type: {}", type);
    }
    
    /**
     * Send a notification based on user's preferred communication method
     * @param userId recipient user ID
     * @param email recipient email
     * @param phoneNumber recipient phone number
     * @param preferredMethod preferred communication method (EMAIL, SMS, BOTH)
     * @param subject notification subject
     * @param message notification message
     */
    public void sendNotificationByPreference(Long userId, String email, String phoneNumber, 
                                            String preferredMethod, String subject, String message) {
        if ("EMAIL".equals(preferredMethod) || "BOTH".equals(preferredMethod)) {
            sendEmailNotification(email, subject, message);
        }
        
        if ("SMS".equals(preferredMethod) || "BOTH".equals(preferredMethod)) {
            sendSmsNotification(phoneNumber, message);
        }
        
        // Always send in-app notification
        sendInAppNotification(userId, subject, message, "SYSTEM");
    }
    
    /**
     * Send appointment confirmation notification
     * @param appointment the appointment
     */
    public void sendAppointmentConfirmation(Appointment appointment) {
        log.info("Sending appointment confirmation for appointment ID: {}", appointment.getId());
        // Implementation would send email, SMS, and in-app notification
    }
    
    /**
     * Send appointment cancellation notification
     * @param appointment the appointment
     */
    public void sendAppointmentCancellation(Appointment appointment) {
        log.info("Sending appointment cancellation for appointment ID: {}", appointment.getId());
        // Implementation would send email, SMS, and in-app notification
    }
    
    /**
     * Send appointment status update notification
     * @param appointment the appointment
     * @param oldStatus the old status
     */
    public void sendAppointmentStatusUpdate(Appointment appointment, Appointment.AppointmentStatus oldStatus) {
        log.info("Sending appointment status update for appointment ID: {} from {} to {}", 
                appointment.getId(), oldStatus, appointment.getStatus());
        // Implementation would send email, SMS, and in-app notification
    }
    
    /**
     * Send appointment reminder notification
     * @param appointment the appointment
     */
    public void sendAppointmentReminder(Appointment appointment) {
        log.info("Sending appointment reminder for appointment ID: {}", appointment.getId());
        // Implementation would send email, SMS, and in-app notification
    }
    
    /**
     * Send lab report notification
     * @param labReport the lab report
     */
    public void sendLabReportNotification(LabReport labReport) {
        log.info("Sending lab report notification for report ID: {}", labReport.getId());
        // Implementation would send email, SMS, and in-app notification
    }
    
    /**
     * Send prescription notification
     * @param prescription the prescription
     */
    public void sendPrescriptionNotification(Prescription prescription) {
        log.info("Sending prescription notification for prescription ID: {}", prescription.getId());
        // Implementation would send email, SMS, and in-app notification
    }
}