package com.ingestion.inventory.controller;

import com.ingestion.inventory.model.*;
import com.ingestion.inventory.service.*;
import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/inventory/dashboard")
public class InventoryDashboardController {

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private StoreService storeService;
    
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    
    //@Autowired
    //private AssetService assetService;

    @GetMapping
    public String getDashboard(Model model) {
        // Get active stores
        List<Store> stores = storeService.findActiveStores();
        model.addAttribute("stores", stores);
        
        // Get low stock items
        List<Item> lowStockItems = itemService.findItemsNeedingReorder();
        model.addAttribute("lowStockItems", lowStockItems);
        
        // Get expiring items (within next 30 days)
        List<Inventory> expiringItems = inventoryService.findExpiringItems(30);
        model.addAttribute("expiringItems", expiringItems);
        
        // Get expired items
        List<Inventory> expiredItems = inventoryService.findExpiredItems();
        model.addAttribute("expiredItems", expiredItems);
        
        // Get pending purchase orders
        List<PurchaseOrder.PurchaseOrderStatus> pendingStatuses = List.of(
                PurchaseOrder.PurchaseOrderStatus.PENDING_APPROVAL,
                PurchaseOrder.PurchaseOrderStatus.APPROVED,
                PurchaseOrder.PurchaseOrderStatus.PLACED,
                PurchaseOrder.PurchaseOrderStatus.PARTIALLY_RECEIVED
        );
        List<PurchaseOrder> pendingOrders = purchaseOrderService.findByStatusesOrderByOrderDate(pendingStatuses);
        model.addAttribute("pendingOrders", pendingOrders);
        
        // Get assets needing maintenance
        //List<Asset> assetsNeedingMaintenance = assetService.findAssetsNeedingMaintenance();
        //model.addAttribute("assetsNeedingMaintenance", assetsNeedingMaintenance);
        
        // Get recent purchase orders (last 30 days)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<PurchaseOrder> recentOrders = purchaseOrderService.findByOrderDateAfter(thirtyDaysAgo);
        model.addAttribute("recentOrders", recentOrders);
        
        // Get item count by category
        model.addAttribute("itemCategories", itemService.getItemCountByCategory());
        
        return "inventory/dashboard";
    }
}