package com.ingestion.inventory.controller;

import com.ingestion.inventory.model.*;
import com.ingestion.inventory.service.*;
import com.ingestion.common.model.purchasing.PurchaseOrder;
import com.ingestion.common.service.PurchaseOrderService;
import com.ingestion.common.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    
    @Autowired
    private SupplierService supplierService;
    
    // @Autowired
    // private AssetService assetService;

    @GetMapping
    public String getDashboard(Model model) {
        // Dashboard Statistics
        Map<String, Object> stats = new HashMap<>();
        
        // Get active stores
        List<Store> stores = storeService.findActiveStores();
        model.addAttribute("stores", stores);
        stats.put("totalStores", stores.size());
        
        // Get low stock items
        List<Item> lowStockItems = itemService.findItemsNeedingReorder();
        model.addAttribute("lowStockItems", lowStockItems);
        stats.put("lowStockItemsCount", lowStockItems.size());
        
        // Get expiring items (within next 30 days)
        List<Inventory> expiringItems = inventoryService.findExpiringItems(30);
        model.addAttribute("expiringItems", expiringItems);
        stats.put("expiringItemsCount", expiringItems.size());
        
        // Get expired items
        List<Inventory> expiredItems = inventoryService.findExpiredItems();
        model.addAttribute("expiredItems", expiredItems);
        stats.put("expiredItemsCount", expiredItems.size());
        
        // Get pending purchase orders
        List<PurchaseOrder.PurchaseOrderStatus> pendingStatuses = List.of(
                PurchaseOrder.PurchaseOrderStatus.PENDING_APPROVAL,
                PurchaseOrder.PurchaseOrderStatus.APPROVED,
                PurchaseOrder.PurchaseOrderStatus.PLACED,
                PurchaseOrder.PurchaseOrderStatus.PARTIALLY_RECEIVED
        );
        List<PurchaseOrder> pendingOrders = purchaseOrderService.findByStatusesOrderByOrderDate(pendingStatuses);
        model.addAttribute("pendingOrders", pendingOrders);
        stats.put("pendingOrdersCount", pendingOrders.size());
        
        // Get assets needing maintenance - commented out until AssetService implementation is available
        // List<Asset> assetsNeedingMaintenance = assetService.findAssetsNeedingMaintenance();
        // model.addAttribute("assetsNeedingMaintenance", assetsNeedingMaintenance);
        // stats.put("assetsNeedingMaintenanceCount", assetsNeedingMaintenance.size());
        model.addAttribute("assetsNeedingMaintenance", List.of()); // Placeholder
        stats.put("assetsNeedingMaintenanceCount", 0); // Placeholder
        
        // Get recent purchase orders (last 30 days)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<PurchaseOrder> recentOrders = purchaseOrderService.findByOrderDateAfter(thirtyDaysAgo);
        model.addAttribute("recentOrders", recentOrders);
        
        // Get item count by category
        List<Object[]> itemCategoriesData = itemService.getItemCountByCategory();
        model.addAttribute("itemCategories", itemCategoriesData);
        stats.put("totalItems", itemService.findAll().size());
        stats.put("totalCategories", itemCategoriesData.size());
        
        // Financial Statistics - using placeholder values
        stats.put("totalInventoryValue", 50000.0); // Placeholder
        stats.put("monthlyPurchaseValue", 10000.0); // Placeholder
        
        // Supplier Statistics - using placeholder values
        stats.put("totalSuppliers", 0); // Placeholder
        stats.put("activeSuppliers", 0); // Placeholder
        
        // Recent Activities - using placeholder data
        model.addAttribute("recentGoodsReceipts", List.of()); // Placeholder
        model.addAttribute("recentStockMovements", List.of()); // Placeholder
        model.addAttribute("topSuppliers", List.of()); // Placeholder
        
        // Chart Data - using placeholder data
        model.addAttribute("monthlyPurchaseData", List.of(5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000, 13000, 14000, 15000, 16000));
        model.addAttribute("categoryWiseInventoryValue", Map.of("Medicines", 25000, "Equipment", 15000, "Supplies", 10000));
        model.addAttribute("stockLevelTrends", List.of()); // Placeholder
        
        model.addAttribute("stats", stats);
        
        return "inventory/dashboard";
    }
}