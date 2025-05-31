package com.ingestion.reception.service.impl;

import com.ingestion.doctor.model.Doctor;
import com.ingestion.doctor.repository.DoctorRepository;
import com.ingestion.reception.dto.AppointmentSlotDTO;
import com.ingestion.reception.model.AppointmentSlot;
import com.ingestion.reception.repository.AppointmentSlotRepository;
import com.ingestion.reception.service.AppointmentSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;
    @Autowired
    private DoctorRepository doctorRepository;



    @Override
    @Transactional
    public AppointmentSlotDTO createAppointmentSlot(AppointmentSlotDTO slotDTO) {
        Doctor doctor = doctorRepository.findById(slotDTO.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + slotDTO.getDoctorId()));
        
        AppointmentSlot slot = new AppointmentSlot();
        slot.setDoctor(doctor);
        slot.setSlotDate(slotDTO.getSlotDate());
        slot.setStartTime(slotDTO.getStartTime());
        slot.setEndTime(slotDTO.getEndTime());
        slot.setAvailable(slotDTO.isAvailable());
        slot.setSlotType(slotDTO.getSlotType());
        slot.setMaxPatients(slotDTO.getMaxPatients());
        slot.setBookedPatients(slotDTO.getBookedPatients());
        slot.setDepartment(slotDTO.getDepartment());
        slot.setLocation(slotDTO.getLocation());
        
        AppointmentSlot savedSlot = appointmentSlotRepository.save(slot);
        
        return convertToDTO(savedSlot);
    }

    @Override
    @Transactional
    public List<AppointmentSlotDTO> createAppointmentSlots(List<AppointmentSlotDTO> slotDTOs) {
        List<AppointmentSlotDTO> createdSlots = new ArrayList<>();
        
        for (AppointmentSlotDTO slotDTO : slotDTOs) {
            createdSlots.add(createAppointmentSlot(slotDTO));
        }
        
        return createdSlots;
    }

    @Override
    public AppointmentSlotDTO getAppointmentSlotById(Long id) {
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment slot not found with ID: " + id));
        
        return convertToDTO(slot);
    }

    @Override
    public List<AppointmentSlotDTO> getSlotsByDoctorAndDate(Long doctorId, LocalDate date) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findByDoctorIdAndSlotDate(doctorId, date);
        
        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentSlotDTO> getSlotsByDoctorAndDateRange(Long doctorId, LocalDate startDate, LocalDate endDate) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findByDoctorIdAndSlotDateBetween(doctorId, startDate, endDate);
        
        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentSlotDTO> getAvailableSlotsByDate(LocalDate date) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findBySlotDateAndIsAvailableTrue(date);
        
        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentSlotDTO> getAvailableSlotsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findBySlotDateBetweenAndIsAvailableTrue(startDate, endDate);
        
        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentSlotDTO> getAvailableSlotsByDateAndType(LocalDate date, AppointmentSlot.SlotType slotType) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findAvailableSlotsByDateAndType(date, slotType);
        
        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentSlotDTO> getAvailableSlotsByDateAndDepartment(LocalDate date, String department) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findAvailableSlotsByDateAndDepartment(date, department);
        
        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentSlotDTO> findSlotsByDoctorAndTime(Long doctorId, LocalDate date, LocalTime time) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findSlotsByDoctorAndDateAndTime(doctorId, date, time);
        
        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentSlotDTO updateAppointmentSlot(Long id, AppointmentSlotDTO slotDTO) {
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment slot not found with ID: " + id));
        
        slot.setSlotDate(slotDTO.getSlotDate());
        slot.setStartTime(slotDTO.getStartTime());
        slot.setEndTime(slotDTO.getEndTime());
        slot.setAvailable(slotDTO.isAvailable());
        slot.setSlotType(slotDTO.getSlotType());
        slot.setMaxPatients(slotDTO.getMaxPatients());
        slot.setBookedPatients(slotDTO.getBookedPatients());
        slot.setDepartment(slotDTO.getDepartment());
        slot.setLocation(slotDTO.getLocation());
        
        AppointmentSlot updatedSlot = appointmentSlotRepository.save(slot);
        
        return convertToDTO(updatedSlot);
    }

    @Override
    @Transactional
    public AppointmentSlotDTO bookSlot(Long id) {
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment slot not found with ID: " + id));
        
        if (!slot.isAvailable() || slot.getBookedPatients() >= slot.getMaxPatients()) {
            throw new IllegalStateException("Slot is not available for booking");
        }
        
        slot.setBookedPatients(slot.getBookedPatients() + 1);
        
        if (slot.getBookedPatients() >= slot.getMaxPatients()) {
            slot.setAvailable(false);
        }
        
        AppointmentSlot updatedSlot = appointmentSlotRepository.save(slot);
        
        return convertToDTO(updatedSlot);
    }

    @Override
    @Transactional
    public AppointmentSlotDTO cancelBooking(Long id) {
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment slot not found with ID: " + id));
        
        if (slot.getBookedPatients() <= 0) {
            throw new IllegalStateException("No bookings to cancel for this slot");
        }
        
        slot.setBookedPatients(slot.getBookedPatients() - 1);
        
        if (!slot.isAvailable() && slot.getBookedPatients() < slot.getMaxPatients()) {
            slot.setAvailable(true);
        }
        
        AppointmentSlot updatedSlot = appointmentSlotRepository.save(slot);
        
        return convertToDTO(updatedSlot);
    }

    @Override
    @Transactional
    public void deleteAppointmentSlot(Long id) {
        if (!appointmentSlotRepository.existsById(id)) {
            throw new EntityNotFoundException("Appointment slot not found with ID: " + id);
        }
        
        appointmentSlotRepository.deleteById(id);
    }

    @Override
    public long countAvailableSlotsByDate(LocalDate date) {
        return appointmentSlotRepository.countAvailableSlotsByDate(date);
    }
    
    private AppointmentSlotDTO convertToDTO(AppointmentSlot slot) {
        AppointmentSlotDTO dto = new AppointmentSlotDTO();
        dto.setId(slot.getId());
        dto.setDoctorId(slot.getDoctor().getId());
        dto.setDoctorName(slot.getDoctor().getFirstName() + " " + slot.getDoctor().getLastName());
        dto.setDoctorSpecialization(slot.getDoctor().getSpecialization());
        dto.setSlotDate(slot.getSlotDate());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        dto.setAvailable(slot.isAvailable());
        dto.setSlotType(slot.getSlotType());
        dto.setMaxPatients(slot.getMaxPatients());
        dto.setBookedPatients(slot.getBookedPatients());
        dto.setDepartment(slot.getDepartment());
        dto.setLocation(slot.getLocation());
        
        return dto;
    }
}