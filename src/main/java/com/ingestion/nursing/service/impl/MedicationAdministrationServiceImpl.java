package com.ingestion.nursing.service.impl;

import com.ingestion.nursing.model.MedicationAdministration;
import com.ingestion.nursing.repository.MedicationAdministrationRepository;
import com.ingestion.nursing.service.MedicationAdministrationService;
import com.ingestion.patient.model.Patient;
import com.ingestion.pharmacy.model.Medication;
import com.ingestion.pharmacy.model.Prescription;
import com.ingestion.pharmacy.model.PrescriptionItem;
import com.ingestion.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicationAdministrationServiceImpl implements MedicationAdministrationService {

    @Autowired
    private MedicationAdministrationRepository medicationAdministrationRepository;

    @Override
    public MedicationAdministration saveMedicationAdministration(MedicationAdministration medicationAdministration) {
        return medicationAdministrationRepository.save(medicationAdministration);
    }

    @Override
    public Optional<MedicationAdministration> findById(Long id) {
        return medicationAdministrationRepository.findById(id);
    }

    @Override
    public List<MedicationAdministration> findByPatient(Patient patient) {
        return medicationAdministrationRepository.findByPatientOrderByScheduledTimeDesc(patient);
    }

    @Override
    public Page<MedicationAdministration> findByPatient(Patient patient, Pageable pageable) {
        return medicationAdministrationRepository.findByPatientOrderByScheduledTimeDesc(patient, pageable);
    }

    @Override
    public List<MedicationAdministration> findByPatientAndStatus(Patient patient, MedicationAdministration.AdministrationStatus status) {
        return medicationAdministrationRepository.findByPatientAndStatusOrderByScheduledTimeDesc(patient, status);
    }

    @Override
    public List<MedicationAdministration> findByPrescription(Prescription prescription) {
        return medicationAdministrationRepository.findByPrescriptionOrderByScheduledTimeDesc(prescription);
    }

    @Override
    public List<MedicationAdministration> findByPrescriptionItem(PrescriptionItem prescriptionItem) {
        return medicationAdministrationRepository.findByPrescriptionItemOrderByScheduledTimeDesc(prescriptionItem);
    }

    @Override
    public List<MedicationAdministration> findByMedication(Medication medication) {
        return medicationAdministrationRepository.findByMedicationOrderByScheduledTimeDesc(medication);
    }

    @Override
    public List<MedicationAdministration> findByAdministeredBy(User nurse) {
        return medicationAdministrationRepository.findByAdministeredByOrderByAdministeredTimeDesc(nurse);
    }

    @Override
    public List<MedicationAdministration> findByStatusAndTimeRange(MedicationAdministration.AdministrationStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        return medicationAdministrationRepository.findByStatusAndScheduledTimeBetweenOrderByScheduledTimeAsc(status, startTime, endTime);
    }

    @Override
    public List<MedicationAdministration> findDueMedications() {
        LocalDateTime now = LocalDateTime.now();
        return medicationAdministrationRepository.findDueMedications(now);
    }

    @Override
    public List<MedicationAdministration> findUpcomingMedications(int hoursAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureTime = now.plusHours(hoursAhead);
        return medicationAdministrationRepository.findUpcomingMedications(now, futureTime);
    }

    @Override
    public List<MedicationAdministration> findOverdueMedications() {
        LocalDateTime now = LocalDateTime.now();
        return medicationAdministrationRepository.findOverdueMedications(now);
    }

    @Override
    public List<MedicationAdministration> findStatMedications() {
        return medicationAdministrationRepository.findStatMedications();
    }

    @Override
    public MedicationAdministration scheduleMedication(Patient patient, Prescription prescription, PrescriptionItem prescriptionItem,
                                                     Medication medication, String dosage, String route, LocalDateTime scheduledTime,
                                                     Boolean isPrn, Boolean isStat, Boolean witnessRequired) {
        MedicationAdministration medAdmin = new MedicationAdministration();
        medAdmin.setPatient(patient);
        medAdmin.setPrescription(prescription);
        medAdmin.setPrescriptionItem(prescriptionItem);
        medAdmin.setMedication(medication);
        medAdmin.setDosage(dosage);
        medAdmin.setRoute(route);
        medAdmin.setScheduledTime(scheduledTime);
        medAdmin.setIsPrn(isPrn);
        medAdmin.setIsStat(isStat);
        medAdmin.setWitnessRequired(witnessRequired);
        medAdmin.setStatus(MedicationAdministration.AdministrationStatus.SCHEDULED);
        medAdmin.setCreatedAt(LocalDateTime.now());
        
        return medicationAdministrationRepository.save(medAdmin);
    }

    @Override
    public MedicationAdministration administerMedication(Long medicationAdministrationId, User nurse, LocalDateTime administeredTime,
                                                       String notes, User witness) {
        Optional<MedicationAdministration> medAdminOpt = medicationAdministrationRepository.findById(medicationAdministrationId);
        if (medAdminOpt.isPresent()) {
            MedicationAdministration medAdmin = medAdminOpt.get();
            medAdmin.setStatus(MedicationAdministration.AdministrationStatus.ADMINISTERED);
            medAdmin.setAdministeredBy(nurse);
            medAdmin.setAdministeredTime(administeredTime);
            medAdmin.setNotes(notes);
            medAdmin.setWitnessedBy(witness);
            return medicationAdministrationRepository.save(medAdmin);
        }
        throw new RuntimeException("Medication administration not found with id: " + medicationAdministrationId);
    }

    @Override
    public MedicationAdministration markAsMissed(Long medicationAdministrationId, String reason) {
        Optional<MedicationAdministration> medAdminOpt = medicationAdministrationRepository.findById(medicationAdministrationId);
        if (medAdminOpt.isPresent()) {
            MedicationAdministration medAdmin = medAdminOpt.get();
            medAdmin.setStatus(MedicationAdministration.AdministrationStatus.MISSED);
            medAdmin.setNotes(reason);
            return medicationAdministrationRepository.save(medAdmin);
        }
        throw new RuntimeException("Medication administration not found with id: " + medicationAdministrationId);
    }

    @Override
    public MedicationAdministration markAsRefused(Long medicationAdministrationId, String reason) {
        Optional<MedicationAdministration> medAdminOpt = medicationAdministrationRepository.findById(medicationAdministrationId);
        if (medAdminOpt.isPresent()) {
            MedicationAdministration medAdmin = medAdminOpt.get();
            medAdmin.setStatus(MedicationAdministration.AdministrationStatus.REFUSED);
            medAdmin.setNotes(reason);
            return medicationAdministrationRepository.save(medAdmin);
        }
        throw new RuntimeException("Medication administration not found with id: " + medicationAdministrationId);
    }

    @Override
    public MedicationAdministration holdMedication(Long medicationAdministrationId, String reason) {
        Optional<MedicationAdministration> medAdminOpt = medicationAdministrationRepository.findById(medicationAdministrationId);
        if (medAdminOpt.isPresent()) {
            MedicationAdministration medAdmin = medAdminOpt.get();
            medAdmin.setStatus(MedicationAdministration.AdministrationStatus.HELD);
            medAdmin.setNotes(reason);
            return medicationAdministrationRepository.save(medAdmin);
        }
        throw new RuntimeException("Medication administration not found with id: " + medicationAdministrationId);
    }

    @Override
    public MedicationAdministration cancelMedication(Long medicationAdministrationId, String reason) {
        Optional<MedicationAdministration> medAdminOpt = medicationAdministrationRepository.findById(medicationAdministrationId);
        if (medAdminOpt.isPresent()) {
            MedicationAdministration medAdmin = medAdminOpt.get();
            medAdmin.setStatus(MedicationAdministration.AdministrationStatus.CANCELLED);
            medAdmin.setNotes(reason);
            return medicationAdministrationRepository.save(medAdmin);
        }
        throw new RuntimeException("Medication administration not found with id: " + medicationAdministrationId);
    }

    @Override
    public void deleteMedicationAdministration(Long id) {
        medicationAdministrationRepository.deleteById(id);
    }
}