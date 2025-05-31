package com.ingestion.laboratory.controller;

import com.ingestion.common.laboratory.LabOrder;
import com.ingestion.common.laboratory.LabOrderService;
import com.ingestion.doctor.model.Doctor;
import com.ingestion.doctor.service.DoctorService;
import com.ingestion.laboratory.model.LabTest;
import com.ingestion.laboratory.service.LabTestService;
import com.ingestion.opd.model.OpdVisit;
import com.ingestion.opd.service.OpdVisitService;
import com.ingestion.patient.model.Patient;
import com.ingestion.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/laboratory/orders")
public class LabOrderController {

    @Autowired
    @Qualifier("commonLabOrderService")
    private LabOrderService labOrderService;
    
    @Autowired
    private LabTestService labTestService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private OpdVisitService opdVisitService;

    @GetMapping
    public String getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String date,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<LabOrder> orders;
        
        if (status != null && !status.isEmpty()) {
            try {
                LabOrder.OrderStatus orderStatus = LabOrder.OrderStatus.valueOf(status);
                orders = Page.empty(pageable); // Placeholder, replace with actual repository method
                List<LabOrder> statusOrders = labOrderService.findByStatus(orderStatus);
                model.addAttribute("orders", statusOrders);
                model.addAttribute("status", status);
            } catch (IllegalArgumentException e) {
                orders = Page.empty(pageable);
                model.addAttribute("errorMessage", "Invalid status");
            }
        } else if (priority != null && !priority.isEmpty()) {
            try {
                LabOrder.OrderPriority orderPriority = LabOrder.OrderPriority.valueOf(priority);
                orders = Page.empty(pageable); // Placeholder, replace with actual repository method
                List<LabOrder> priorityOrders = labOrderService.findByPriority(orderPriority);
                model.addAttribute("orders", priorityOrders);
                model.addAttribute("priority", priority);
            } catch (IllegalArgumentException e) {
                orders = Page.empty(pageable);
                model.addAttribute("errorMessage", "Invalid priority");
            }
        } else if (date != null && !date.isEmpty()) {
            try {
                LocalDate orderDate = LocalDate.parse(date);
                orders = Page.empty(pageable); // Placeholder, replace with actual repository method
                List<LabOrder> dateOrders = labOrderService.findByOrderDate(orderDate);
                model.addAttribute("orders", dateOrders);
                model.addAttribute("date", date);
            } catch (Exception e) {
                orders = Page.empty(pageable);
                model.addAttribute("errorMessage", "Invalid date format. Use yyyy-MM-dd");
            }
        } else {
            // Get all orders
            List<LabOrder> allOrders = labOrderService.findAll();
            model.addAttribute("orders", allOrders);
        }
        
        model.addAttribute("statuses", LabOrder.OrderStatus.values());
        model.addAttribute("priorities", LabOrder.OrderPriority.values());
        
        return "laboratory/orders/list";
    }

    @GetMapping("/pending")
    public String getPendingOrders(Model model) {
        List<LabOrder.OrderStatus> pendingStatuses = Arrays.asList(
                LabOrder.OrderStatus.ORDERED,
                LabOrder.OrderStatus.SAMPLE_COLLECTED,
                LabOrder.OrderStatus.IN_PROCESS
        );
        List<LabOrder> pendingOrders = labOrderService.findByStatusesOrderByPriorityAndOrderDate(pendingStatuses);
        model.addAttribute("orders", pendingOrders);
        model.addAttribute("title", "Pending Orders");
        return "laboratory/orders/list";
    }

    @GetMapping("/today")
    public String getTodayOrders(Model model) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        List<LabOrder> todayOrders = labOrderService.findByOrderDateRange(startOfDay, endOfDay);
        model.addAttribute("orders", todayOrders);
        model.addAttribute("title", "Today's Orders");
        return "laboratory/orders/list";
    }

    @GetMapping("/new")
    public String showCreateForm(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long visitId,
            Model model) {
        
        LabOrder labOrder = new LabOrder();
        
        // If patient ID is provided, pre-select the patient
        if (patientId != null) {
            Optional<Patient> patientOpt = patientService.findById(patientId);
            patientOpt.ifPresent(labOrder::setPatient);
        }
        
        // If visit ID is provided, pre-select the visit and its associated patient and doctor
        if (visitId != null) {
            Optional<OpdVisit> visitOpt = opdVisitService.findById(visitId);
            if (visitOpt.isPresent()) {
                OpdVisit visit = visitOpt.get();
                labOrder.setOpdVisit(visit);
                labOrder.setPatient(visit.getPatient());
                labOrder.setDoctor(visit.getDoctor());
            }
        }
        
        model.addAttribute("labOrder", labOrder);
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("tests", labTestService.findAllActiveTests());
        model.addAttribute("priorities", LabOrder.OrderPriority.values());
        
        return "laboratory/orders/create";
    }

    @PostMapping
    public String createOrder(@Valid @ModelAttribute("labOrder") LabOrder labOrder,
                             BindingResult result,
                             @RequestParam(value = "selectedTests", required = false) List<Long> selectedTestIds,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("tests", labTestService.findAllActiveTests());
            model.addAttribute("priorities", LabOrder.OrderPriority.values());
            return "laboratory/orders/create";
        }

        try {
            // Get the selected tests
            List<LabTest> selectedTests = new ArrayList<>();
            if (selectedTestIds != null && !selectedTestIds.isEmpty()) {
                for (Long testId : selectedTestIds) {
                    Optional<LabTest> testOpt = labTestService.findById(testId);
                    testOpt.ifPresent(selectedTests::add);
                }
            }
            
            // Create the order
            LabOrder savedOrder = labOrderService.createOrder(
                    labOrder.getPatient(),
                    labOrder.getDoctor(),
                    labOrder.getOpdVisit(),
                    labOrder.getPriority(),
                    labOrder.getClinicalNotes(),
                    labOrder.getSpecialInstructions(),
                    selectedTests
            );
            
            redirectAttributes.addFlashAttribute("successMessage", "Order created successfully");
            return "redirect:/laboratory/orders/" + savedOrder.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating order: " + e.getMessage());
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("tests", labTestService.findAllActiveTests());
            model.addAttribute("priorities", LabOrder.OrderPriority.values());
            return "laboratory/orders/create";
        }
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, Model model) {
        Optional<LabOrder> orderOpt = labOrderService.findById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("labOrder", orderOpt.get());
            return "laboratory/orders/view";
        } else {
            model.addAttribute("errorMessage", "Order not found");
            return "redirect:/laboratory/orders";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<LabOrder> orderOpt = labOrderService.findById(id);
        if (orderOpt.isPresent()) {
            LabOrder order = orderOpt.get();
            
            // Only allow editing if the order is in ORDERED status
            if (order.getStatus() != LabOrder.OrderStatus.ORDERED) {
                model.addAttribute("errorMessage", "Cannot edit order that is not in ORDERED status");
                return "redirect:/laboratory/orders/" + id;
            }
            
            model.addAttribute("labOrder", order);
            model.addAttribute("patients", patientService.findAll());
            model.addAttribute("doctors", doctorService.findAll());
            model.addAttribute("tests", labTestService.findAllActiveTests());
            model.addAttribute("priorities", LabOrder.OrderPriority.values());
            
            return "laboratory/orders/edit";
        } else {
            model.addAttribute("errorMessage", "Order not found");
            return "redirect:/laboratory/orders";
        }
    }

    @PostMapping("/{id}/add-test")
    public String addTestToOrder(@PathVariable Long id,
                                @RequestParam Long testId,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<LabTest> testOpt = labTestService.findById(testId);
            if (testOpt.isPresent()) {
                labOrderService.addTestToOrder(id, testOpt.get());
                redirectAttributes.addFlashAttribute("successMessage", "Test added to order successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Test not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding test to order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{orderId}/remove-test/{itemId}")
    public String removeTestFromOrder(@PathVariable Long orderId,
                                     @PathVariable Long itemId,
                                     RedirectAttributes redirectAttributes) {
        try {
            labOrderService.removeTestFromOrder(orderId, itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Test removed from order successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error removing test from order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + orderId;
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                   @RequestParam LabOrder.OrderStatus status,
                                   RedirectAttributes redirectAttributes) {
        try {
            labOrderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/sample-collected")
    public String markAsSampleCollected(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.markAsSampleCollected(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order marked as sample collected");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/in-process")
    public String markAsInProcess(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.markAsInProcess(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order marked as in process");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/completed")
    public String markAsCompleted(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.markAsCompleted(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order marked as completed");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/cancelled")
    public String markAsCancelled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.markAsCancelled(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order marked as cancelled");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/rejected")
    public String markAsRejected(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.markAsRejected(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order marked as rejected");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/billed")
    public String markAsBilled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.markAsBilled(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order marked as billed");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/paid")
    public String markAsPaid(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.markAsPaid(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order marked as paid");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order: " + e.getMessage());
        }
        return "redirect:/laboratory/orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labOrderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully");
            return "redirect:/laboratory/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting order: " + e.getMessage());
            return "redirect:/laboratory/orders/" + id;
        }
    }
}