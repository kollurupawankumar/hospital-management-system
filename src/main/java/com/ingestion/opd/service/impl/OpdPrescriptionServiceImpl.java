package com.ingestion.opd.service.impl;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.opd.model.OpdMedication;
import com.ingestion.opd.model.OpdPrescription;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.opd.repository.OpdPrescriptionRepository;
import com.ingestion.opd.service.OpdPrescriptionService;
import com.ingestion.patient.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class OpdPrescriptionServiceImpl implements OpdPrescriptionService {

    @Autowired
    private OpdPrescriptionRepository prescriptionRepository;

    @Override
    @Transactional
    public OpdPrescription savePrescription(OpdPrescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @Override
    public Optional<OpdPrescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public Optional<OpdPrescription> findByPrescriptionNumber(String prescriptionNumber) {
        return prescriptionRepository.findByPrescriptionNumber(prescriptionNumber);
    }

    @Override
    public List<OpdPrescription> findAll() {
        return prescriptionRepository.findAll();
    }

    @Override
    public List<OpdPrescription> findByOpdVisit(OpdVisit opdVisit) {
        return prescriptionRepository.findByOpdVisit(opdVisit);
    }

    @Override
    public List<OpdPrescription> findByDoctor(Doctor doctor) {
        return prescriptionRepository.findByDoctor(doctor);
    }

    @Override
    public List<OpdPrescription> findByPatient(Patient patient) {
        return prescriptionRepository.findByPatient(patient);
    }

    @Override
    public List<OpdPrescription> findByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return prescriptionRepository.findByPrescriptionDateBetween(startOfDay, endOfDay);
    }

    @Override
    public List<OpdPrescription> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return prescriptionRepository.findByPrescriptionDateBetween(startDate, endDate);
    }

    @Override
    public List<OpdPrescription> findByDoctorAndDateRange(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate) {
        return prescriptionRepository.findByDoctorAndDateRange(doctor, startDate, endDate);
    }

    @Override
    public List<OpdPrescription> findByPatientAndDateRange(Patient patient, LocalDateTime startDate, LocalDateTime endDate) {
        return prescriptionRepository.findByPatientAndDateRange(patient, startDate, endDate);
    }

    @Override
    @Transactional
    public OpdPrescription createPrescription(OpdVisit opdVisit, Doctor doctor, String diagnosis, String notes, List<OpdMedication> medications) {
        OpdPrescription prescription = new OpdPrescription();
        prescription.setOpdVisit(opdVisit);
        prescription.setDoctor(doctor);
        prescription.setDiagnosis(diagnosis);
        prescription.setNotes(notes);
        prescription.setPrescriptionDate(LocalDateTime.now());
        
        // Save the prescription first
        OpdPrescription savedPrescription = prescriptionRepository.save(prescription);
        
        // Add medications
        if (medications != null && !medications.isEmpty()) {
            for (OpdMedication medication : medications) {
                medication.setPrescription(savedPrescription);
                savedPrescription.getMedications().add(medication);
            }
        }
        
        // Add the prescription to the visit
        opdVisit.addPrescription(savedPrescription);
        
        return prescriptionRepository.save(savedPrescription);
    }

    @Override
    @Transactional
    public OpdPrescription addMedicationToPrescription(Long prescriptionId, OpdMedication medication) {
        Optional<OpdPrescription> optionalPrescription = prescriptionRepository.findById(prescriptionId);
        if (optionalPrescription.isPresent()) {
            OpdPrescription prescription = optionalPrescription.get();
            prescription.addMedication(medication);
            return prescriptionRepository.save(prescription);
        }
        throw new RuntimeException("Prescription not found with id: " + prescriptionId);
    }

    @Override
    @Transactional
    public OpdPrescription removeMedicationFromPrescription(Long prescriptionId, Long medicationId) {
        Optional<OpdPrescription> optionalPrescription = prescriptionRepository.findById(prescriptionId);
        if (optionalPrescription.isPresent()) {
            OpdPrescription prescription = optionalPrescription.get();
            
            // Find the medication to remove
            Optional<OpdMedication> medicationToRemove = prescription.getMedications().stream()
                    .filter(med -> med.getId().equals(medicationId))
                    .findFirst();
            
            if (medicationToRemove.isPresent()) {
                prescription.removeMedication(medicationToRemove.get());
                return prescriptionRepository.save(prescription);
            } else {
                throw new RuntimeException("Medication not found with id: " + medicationId);
            }
        }
        throw new RuntimeException("Prescription not found with id: " + prescriptionId);
    }

    @Override
    @Transactional
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}