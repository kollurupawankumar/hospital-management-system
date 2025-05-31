package com.ingestion.nursing.service.impl;

import com.ingestion.common.model.inpatient.VitalSign;
import com.ingestion.nursing.repository.VitalSignRepository;
import com.ingestion.nursing.service.VitalSignService;
import com.ingestion.patient.model.Patient;
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
public class VitalSignServiceImpl implements VitalSignService {

    @Autowired
    private VitalSignRepository vitalSignRepository;

    @Override
    public VitalSign saveVitalSign(VitalSign vitalSign) {
        return vitalSignRepository.save(vitalSign);
    }

    @Override
    public Optional<VitalSign> findById(Long id) {
        return vitalSignRepository.findById(id);
    }

    @Override
    public List<VitalSign> findByPatient(Patient patient) {
        return vitalSignRepository.findByPatientOrderByRecordedAtDesc(patient);
    }

    @Override
    public Page<VitalSign> findByPatient(Patient patient, Pageable pageable) {
        return vitalSignRepository.findByPatientOrderByRecordedAtDesc(patient, pageable);
    }

    @Override
    public List<VitalSign> findByPatientAndDateRange(Patient patient, LocalDateTime startDate, LocalDateTime endDate) {
        return vitalSignRepository.findByPatientAndRecordedAtBetweenOrderByRecordedAtDesc(patient, startDate, endDate);
    }

    @Override
    public List<VitalSign> findAbnormalVitalsByPatient(Patient patient) {
        return vitalSignRepository.findByIsAbnormalTrueAndPatientOrderByRecordedAtDesc(patient);
    }

    @Override
    public VitalSign findLatestByPatient(Patient patient) {
        return vitalSignRepository.findLatestByPatient(patient);
    }

    @Override
    public List<VitalSign> findRecentAbnormalVitals(int hoursBack) {
        LocalDateTime since = LocalDateTime.now().minusHours(hoursBack);
        return vitalSignRepository.findRecentAbnormalVitals(since);
    }

    @Override
    @Transactional
    public VitalSign recordVitalSigns(Patient patient, Double temperature, String temperatureUnit,
                                    Integer pulseRate, Integer respiratoryRate, Integer bloodPressureSystolic,
                                    Integer bloodPressureDiastolic, Integer oxygenSaturation, Integer painScore,
                                    Double height, Double weight, Integer bloodGlucose, String notes, User recordedBy) {
        
        VitalSign vitalSign = new VitalSign();
        vitalSign.setPatient(patient);
        vitalSign.setTemperature(temperature);
        vitalSign.setTemperatureUnit(temperatureUnit);
        vitalSign.setPulseRate(pulseRate);
        vitalSign.setRespiratoryRate(respiratoryRate);
        vitalSign.setBloodPressureSystolic(bloodPressureSystolic);
        vitalSign.setBloodPressureDiastolic(bloodPressureDiastolic);
        vitalSign.setOxygenSaturation(oxygenSaturation);
        vitalSign.setPainLevel(painScore);
        vitalSign.setHeight(height);
        vitalSign.setWeight(weight);
        vitalSign.setBloodGlucose(bloodGlucose != null ? bloodGlucose.doubleValue() : null);
        vitalSign.setNotes(notes);
        vitalSign.setRecordedByUser(recordedBy);
        vitalSign.setRecordedBy(recordedBy != null ? recordedBy.getUsername() : null);
        vitalSign.setRecordedAt(LocalDateTime.now());
        
        return vitalSignRepository.save(vitalSign);
    }

    @Override
    @Transactional
    public void deleteVitalSign(Long id) {
        vitalSignRepository.deleteById(id);
    }
}