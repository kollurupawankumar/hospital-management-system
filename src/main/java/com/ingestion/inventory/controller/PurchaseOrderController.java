package com.ingestion.inventory.controller;

import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.model.supplier.Supplier;
import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.Store;
import com.ingestion.inventory.service.ItemService;
import com.ingestion.common.service.PurchaseOrderService;
import com.ingestion.inventory.service.StoreService;
import com.ingestion.common.service.SupplierService;
import com.ingestion.security.model.User;
import com.ingestion.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory/orders")
public class PurchaseOrderController {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderController.class);

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listPurchaseOrders(@RequestParam(required = false) String status,
                                   @RequestParam(required = false) Long supplierId,
                                   @RequestParam(required = false) Long storeId,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                   Model model) {
        log.debug("Listing purchase orders with filters - status: {}, supplier: {}, store: {}", 
                 status, supplierId, storeId);

        List<PurchaseOrder> orders;
        
        if (status != null && !status.isEmpty()) {
            try {
                PurchaseOrder.PurchaseOrderStatus orderStatus = PurchaseOrder.PurchaseOrderStatus.valueOf(status);
                orders = purchaseOrderService.findByStatus(orderStatus);
            } catch (IllegalArgumentException e) {
                orders = purchaseOrderService.findAll();
            }
        } else if (supplierId != null) {
            // Find orders by supplier - need to implement this properly
            orders = purchaseOrderService.findAll();
        } else if (storeId != null) {
            // Find orders by store - need to implement this properly
            orders = purchaseOrderService.findAll();
        } else if (startDate != null && endDate != null) {
            orders = purchaseOrderService.findByOrderDateBetween(startDate, endDate);
        } else {
            orders = purchaseOrderService.findAll();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("orderStatuses", PurchaseOrder.PurchaseOrderStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedSupplierId", supplierId);
        model.addAttribute("selectedStoreId", storeId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "inventory/orders/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.debug("Showing create purchase order form");
        
        model.addAttribute("purchaseOrder", new PurchaseOrder());
        // model.addAttribute("suppliers", supplierService.findActiveSuppliers());
        model.addAttribute("suppliers", List.of()); // Placeholder until SupplierService is implemented
        model.addAttribute("stores", storeService.findActiveStores());
        
        return "inventory/orders/form";
    }

    @PostMapping("/new")
    public String createPurchaseOrder(@Valid @ModelAttribute("purchaseOrder") PurchaseOrder purchaseOrder,
                                    BindingResult result,
                                    @RequestParam Long supplierId,
                                    @RequestParam Long storeId,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        log.debug("Creating new purchase order for supplier: {}, store: {}", supplierId, storeId);

        if (result.hasErrors()) {
            // model.addAttribute("suppliers", supplierService.findActiveSuppliers());
            model.addAttribute("suppliers", List.of()); // Placeholder
            model.addAttribute("stores", storeService.findActiveStores());
            return "inventory/orders/form";
        }

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            
            // Find supplier and store - using placeholders for now
            // Supplier supplier = supplierService.findById(supplierId).orElseThrow();
            Store store = storeService.findById(storeId).orElseThrow();
            
            // For now, create a basic purchase order without supplier
            PurchaseOrder savedOrder = new PurchaseOrder();
            savedOrder.setStoreId(storeId);
            savedOrder.setExpectedDeliveryDate(purchaseOrder.getExpectedDeliveryDate());
            savedOrder.setPaymentTerms(purchaseOrder.getPaymentTerms());
            savedOrder.setShippingTerms(purchaseOrder.getShippingTerms());
            savedOrder.setNotes(purchaseOrder.getNotes());
            savedOrder.setOrderType(PurchaseOrder.OrderType.INVENTORY);
            savedOrder.setStatus(PurchaseOrder.PurchaseOrderStatus.DRAFT);
            savedOrder.setCreatedBy(currentUser.getUsername());
            
            savedOrder = purchaseOrderService.savePurchaseOrder(savedOrder);

            redirectAttributes.addFlashAttribute("successMessage", 
                "Purchase Order created successfully with PO Number: " + savedOrder.getPoNumber());
            return "redirect:/inventory/orders/" + savedOrder.getId();

        } catch (Exception e) {
            log.error("Error creating purchase order", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error creating purchase order: " + e.getMessage());
            return "redirect:/inventory/orders/new";
        }
    }

    @GetMapping("/{id}")
    public String viewPurchaseOrder(@PathVariable Long id, Model model) {
        log.debug("Viewing purchase order with ID: {}", id);

        Optional<PurchaseOrder> orderOpt = purchaseOrderService.findById(id);
        if (orderOpt.isEmpty()) {
            return "redirect:/inventory/orders?error=Purchase Order not found";
        }

        PurchaseOrder order = orderOpt.get();
        model.addAttribute("order", order);
        model.addAttribute("items", itemService.findActiveItems());

        return "inventory/orders/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.debug("Showing edit form for purchase order ID: {}", id);

        Optional<PurchaseOrder> orderOpt = purchaseOrderService.findById(id);
        if (orderOpt.isEmpty()) {
            return "redirect:/inventory/orders?error=Purchase Order not found";
        }

        PurchaseOrder order = orderOpt.get();
        
        // Only allow editing if order is in DRAFT status
        if (order.getStatus() != PurchaseOrder.PurchaseOrderStatus.DRAFT) {
            return "redirect:/inventory/orders/" + id + "?error=Cannot edit order in " + order.getStatus() + " status";
        }

        model.addAttribute("purchaseOrder", order);
        // model.addAttribute("suppliers", supplierService.findActiveSuppliers());
        model.addAttribute("suppliers", List.of()); // Placeholder
        model.addAttribute("stores", storeService.findActiveStores());

        return "inventory/orders/form";
    }

    @PostMapping("/{id}/add-item")
    public String addItemToOrder(@PathVariable Long id,
                                @RequestParam Long itemId,
                                @RequestParam Integer quantity,
                                @RequestParam BigDecimal unitPrice,
                                @RequestParam(defaultValue = "0") BigDecimal discountPercentage,
                                @RequestParam(defaultValue = "0") BigDecimal taxPercentage,
                                RedirectAttributes redirectAttributes) {
        log.debug("Adding item {} to purchase order {}", itemId, id);

        try {
            Optional<Item> itemOpt = itemService.findById(itemId);
            if (itemOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Item not found");
                return "redirect:/inventory/orders/" + id;
            }

            Item item = itemOpt.get();
            PurchaseOrder updatedOrder = purchaseOrderService.addItemToPurchaseOrder(
                id, item.getId(), null, // medicineId is null for inventory items
                item.getName(), item.getItemCode(), item.getDescription(),
                item.getUnitOfMeasure(), quantity, unitPrice, discountPercentage, taxPercentage);

            redirectAttributes.addFlashAttribute("successMessage", 
                "Item added to purchase order successfully!");

        } catch (Exception e) {
            log.error("Error adding item to purchase order", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error adding item: " + e.getMessage());
        }

        return "redirect:/inventory/orders/" + id;
    }

    @PostMapping("/{id}/remove-item/{itemId}")
    public String removeItemFromOrder(@PathVariable Long id,
                                    @PathVariable Long itemId,
                                    RedirectAttributes redirectAttributes) {
        log.debug("Removing item {} from purchase order {}", itemId, id);

        try {
            purchaseOrderService.removeItemFromPurchaseOrder(id, itemId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Item removed from purchase order successfully!");

        } catch (Exception e) {
            log.error("Error removing item from purchase order", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error removing item: " + e.getMessage());
        }

        return "redirect:/inventory/orders/" + id;
    }

    @PostMapping("/{id}/submit")
    public String submitForApproval(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        log.debug("Submitting purchase order {} for approval", id);

        try {
            PurchaseOrder submittedOrder = purchaseOrderService.submitForApproval(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Purchase Order submitted for approval successfully!");

        } catch (Exception e) {
            log.error("Error submitting purchase order for approval", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error submitting order: " + e.getMessage());
        }

        return "redirect:/inventory/orders/" + id;
    }

    @PostMapping("/{id}/approve")
    public String approvePurchaseOrder(@PathVariable Long id,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        log.debug("Approving purchase order {}", id);

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            PurchaseOrder approvedOrder = purchaseOrderService.approve(id, currentUser);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Purchase Order approved successfully!");

        } catch (Exception e) {
            log.error("Error approving purchase order", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error approving order: " + e.getMessage());
        }

        return "redirect:/inventory/orders/" + id;
    }

    @PostMapping("/{id}/place")
    public String placeOrder(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        log.debug("Placing purchase order {}", id);

        try {
            PurchaseOrder placedOrder = purchaseOrderService.placeOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Purchase Order placed successfully!");

        } catch (Exception e) {
            log.error("Error placing purchase order", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error placing order: " + e.getMessage());
        }

        return "redirect:/inventory/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelPurchaseOrder(@PathVariable Long id,
                                    @RequestParam String reason,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        log.debug("Cancelling purchase order {} with reason: {}", id, reason);

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            PurchaseOrder cancelledOrder = purchaseOrderService.cancel(id, currentUser, reason);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Purchase Order cancelled successfully!");

        } catch (Exception e) {
            log.error("Error cancelling purchase order", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error cancelling order: " + e.getMessage());
        }

        return "redirect:/inventory/orders/" + id;
    }

    @GetMapping("/overdue")
    public String listOverdueOrders(Model model) {
        log.debug("Listing overdue purchase orders");
        
        List<PurchaseOrder> overdueOrders = purchaseOrderService.findOverduePurchaseOrders();
        model.addAttribute("orders", overdueOrders);
        model.addAttribute("pageTitle", "Overdue Purchase Orders");
        
        return "inventory/orders/list";
    }

    @GetMapping("/pending")
    public String listPendingOrders(Model model) {
        log.debug("Listing pending purchase orders");
        
        List<PurchaseOrder.PurchaseOrderStatus> pendingStatuses = List.of(
            PurchaseOrder.PurchaseOrderStatus.PENDING_APPROVAL,
            PurchaseOrder.PurchaseOrderStatus.APPROVED,
            PurchaseOrder.PurchaseOrderStatus.PLACED,
            PurchaseOrder.PurchaseOrderStatus.PARTIALLY_RECEIVED
        );
        
        List<PurchaseOrder> pendingOrders = purchaseOrderService.findByStatusesOrderByOrderDate(pendingStatuses);
        model.addAttribute("orders", pendingOrders);
        model.addAttribute("pageTitle", "Pending Purchase Orders");
        
        return "inventory/orders/list";
    }
}