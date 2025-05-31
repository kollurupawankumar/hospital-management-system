package com.ingestion.inventory.controller;

import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.common.service.SupplierService;
import com.ingestion.security.model.User;
import com.ingestion.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/inventory/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.findAll());
        return "inventory/suppliers/list";
    }

    @GetMapping("/active")
    public String getActiveSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.findActiveSuppliers());
        model.addAttribute("title", "Active Suppliers");
        return "inventory/suppliers/list";
    }

    @GetMapping("/inactive")
    public String getInactiveSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.findInactiveSuppliers());
        model.addAttribute("title", "Inactive Suppliers");
        return "inventory/suppliers/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "inventory/suppliers/create";
    }

    @PostMapping
    public String createSupplier(@Valid @ModelAttribute("supplier") Supplier supplier,
                               BindingResult result,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inventory/suppliers/create";
        }

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            
            supplierService.createSupplier(
                    supplier.getName(),
                    supplier.getContactPerson(),
                    supplier.getEmail(),
                    supplier.getPhone(),
                    supplier.getAddressLine1(),
                    supplier.getAddressLine2(),
                    supplier.getCity(),
                    supplier.getState(),
                    supplier.getPostalCode(),
                    supplier.getCountry(),
                    Supplier.SupplierType.INVENTORY,
                    supplier.getCategories(),
                    currentUser
            );
            
            redirectAttributes.addFlashAttribute("successMessage", "Supplier created successfully");
            return "redirect:/inventory/suppliers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating supplier: " + e.getMessage());
            return "redirect:/inventory/suppliers/new";
        }
    }

    @GetMapping("/{id}")
    public String getSupplier(@PathVariable Long id, Model model) {
        Optional<Supplier> supplierOpt = supplierService.findById(id);
        if (supplierOpt.isPresent()) {
            model.addAttribute("supplier", supplierOpt.get());
            return "inventory/suppliers/view";
        } else {
            model.addAttribute("errorMessage", "Supplier not found");
            return "redirect:/inventory/suppliers";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Supplier> supplierOpt = supplierService.findById(id);
        if (supplierOpt.isPresent()) {
            model.addAttribute("supplier", supplierOpt.get());
            return "inventory/suppliers/edit";
        } else {
            model.addAttribute("errorMessage", "Supplier not found");
            return "redirect:/inventory/suppliers";
        }
    }

    @PostMapping("/{id}")
    public String updateSupplier(@PathVariable Long id,
                               @Valid @ModelAttribute("supplier") Supplier supplier,
                               BindingResult result,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "inventory/suppliers/edit";
        }

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            
            supplierService.updateSupplier(
                    id,
                    supplier.getName(),
                    supplier.getContactPerson(),
                    supplier.getEmail(),
                    supplier.getPhone(),
                    supplier.getAddressLine1(),
                    supplier.getAddressLine2(),
                    supplier.getCity(),
                    supplier.getState(),
                    supplier.getPostalCode(),
                    supplier.getCountry(),
                    currentUser
            );
            
            redirectAttributes.addFlashAttribute("successMessage", "Supplier updated successfully");
            return "redirect:/inventory/suppliers/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating supplier: " + e.getMessage());
            return "redirect:/inventory/suppliers/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/activate")
    public String activateSupplier(@PathVariable Long id,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.findByUsername(authentication.getName());
            
            supplierService.activateSupplier(id, currentUser);
            
            redirectAttributes.addFlashAttribute("successMessage", "Supplier activated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error activating supplier: " + e.getMessage());
        }
        return "redirect:/inventory/suppliers/" + id;
    }

    @PostMapping("/{id}/deactivate")
    public String deactivateSupplier(@PathVariable Long id,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.findByUsername(authentication.getName());
            
            supplierService.deactivateSupplier(id, currentUser);
            
            redirectAttributes.addFlashAttribute("successMessage", "Supplier deactivated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deactivating supplier: " + e.getMessage());
        }
        return "redirect:/inventory/suppliers/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteSupplier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.deleteSupplier(id);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting supplier: " + e.getMessage());
        }
        return "redirect:/inventory/suppliers";
    }

    @GetMapping("/search")
    public String searchSuppliers(@RequestParam String query, Model model) {
        model.addAttribute("suppliers", supplierService.findByNameContaining(query));
        model.addAttribute("searchQuery", query);
        return "inventory/suppliers/list";
    }
}