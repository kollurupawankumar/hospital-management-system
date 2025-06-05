package com.ingestion.nursing.controller;

import com.ingestion.nursing.model.MedicationAdministration;
import com.ingestion.nursing.model.NursingTask;
import com.ingestion.nursing.model.ShiftHandover;
import com.ingestion.nursing.model.ShiftHandoverPatient;
import com.ingestion.common.model.inpatient.VitalSign;
import com.ingestion.nursing.service.MedicationAdministrationService;
import com.ingestion.nursing.service.NursingTaskService;
import com.ingestion.nursing.service.ShiftHandoverService;
import com.ingestion.nursing.service.VitalSignService;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.service.PatientService;
import com.ingestion.security.model.User;
import com.ingestion.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequestMapping("/nursing")
public class NursingDashboardController {

    @Autowired
    private NursingTaskService nursingTaskService;
    
    @Autowired
    private MedicationAdministrationService medicationAdministrationService;
    
    @Autowired
    private VitalSignService vitalSignService;
    
    @Autowired
    private ShiftHandoverService shiftHandoverService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        
        // Get tasks assigned to the current nurse
        List<NursingTask> assignedTasks = nursingTaskService.findByAssignedTo(currentUser);
        model.addAttribute("assignedTasks", assignedTasks);
        
        // Get due tasks
        List<NursingTask> dueTasks = nursingTaskService.findDueTasks();
        model.addAttribute("dueTasks", dueTasks);
        
        // Get overdue tasks
        List<NursingTask> overdueTasks = nursingTaskService.findOverdueTasks();
        model.addAttribute("overdueTasks", overdueTasks);
        
        // Get high priority tasks
        List<NursingTask> highPriorityTasks = nursingTaskService.findHighPriorityTasks();
        model.addAttribute("highPriorityTasks", highPriorityTasks);
        
        // Get due medications
        List<MedicationAdministration> dueMedications = medicationAdministrationService.findDueMedications();
        model.addAttribute("dueMedications", dueMedications);
        
        // Get overdue medications
        List<MedicationAdministration> overdueMedications = medicationAdministrationService.findOverdueMedications();
        model.addAttribute("overdueMedications", overdueMedications);
        
        // Get STAT medications
        List<MedicationAdministration> statMedications = medicationAdministrationService.findStatMedications();
        model.addAttribute("statMedications", statMedications);
        
        // Get recent abnormal vitals (last 24 hours)
        List<VitalSign> abnormalVitals = vitalSignService.findRecentAbnormalVitals(24);
        model.addAttribute("abnormalVitals", abnormalVitals);
        
        // Get unacknowledged handovers
        List<ShiftHandover> unacknowledgedHandovers = shiftHandoverService.findUnacknowledgedHandoversByNurse(currentUser);
        model.addAttribute("unacknowledgedHandovers", unacknowledgedHandovers);
        
        // Get today's handovers
        List<ShiftHandover> todayHandovers = shiftHandoverService.findTodayHandovers();
        model.addAttribute("todayHandovers", todayHandovers);
        
        // Get recent critical patients (last 24 hours)
        List<ShiftHandoverPatient> criticalPatients = shiftHandoverService.findRecentCriticalPatients(24);
        model.addAttribute("criticalPatients", criticalPatients);
        
        // Get recently admitted patients
        List<Patient> recentPatients = patientService.findRecentlyAdmittedPatients(10);
        model.addAttribute("recentPatients", recentPatients);
        
        return "nursing/dashboard";
    }

    // Nurse Management endpoints
    @GetMapping("/nurses")
    public String getAllNurses(Model model) {
        // Get all users with NURSE role
        List<User> allUsers = userService.getAllUsers();
        List<User> nurses = allUsers.stream()
            .filter(user -> user.getRoles().contains(User.UserRole.ROLE_NURSE))
            .toList();
        model.addAttribute("nurses", nurses);
        return "nursing/nurses";
    }

    @GetMapping("/nurses/new")
    public String newNurse(Model model) {
        model.addAttribute("nurse", new User());
        return "nursing/nurse-form";
    }

    @GetMapping("/tasks")
    public String getTasks(Model model, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        
        // Get all tasks for the current nurse
        List<NursingTask> tasks = nursingTaskService.findByAssignedTo(currentUser);
        model.addAttribute("tasks", tasks);
        
        return "nursing/tasks";
    }
}