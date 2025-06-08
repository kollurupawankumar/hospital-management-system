package com.ingestion.inventory.controller;

import com.ingestion.inventory.model.Item;
import com.ingestion.inventory.model.ItemCategory;
import com.ingestion.inventory.service.ItemService;
import com.ingestion.security.model.User;
import com.ingestion.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory/items")
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listItems(@RequestParam(required = false) String search,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String status,
                           Model model) {
        log.debug("Listing items with filters - search: {}, category: {}, type: {}, status: {}", 
                 search, category, type, status);

        List<Item> items;
        
        if (search != null && !search.trim().isEmpty()) {
            items = itemService.findByNameContaining(search.trim());
        } else if ("active".equals(status)) {
            items = itemService.findActiveItems();
        } else if ("inactive".equals(status)) {
            items = itemService.findInactiveItems();
        } else if ("perishable".equals(status)) {
            items = itemService.findPerishableItems();
        } else if ("reorder".equals(status)) {
            items = itemService.findItemsNeedingReorder();
        } else {
            items = itemService.findAll();
        }

        model.addAttribute("items", items);
        model.addAttribute("itemTypes", Item.ItemType.values());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedStatus", status);

        return "inventory/items/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        log.debug("Showing create item form");
        model.addAttribute("item", new Item());
        model.addAttribute("itemTypes", Item.ItemType.values());
        return "inventory/items/form";
    }

    @PostMapping("/new")
    public String createItem(@Valid @ModelAttribute("item") Item item,
                           BindingResult result,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        log.debug("Creating new item: {}", item.getName());

        if (result.hasErrors()) {
            model.addAttribute("itemTypes", Item.ItemType.values());
            return "inventory/items/form";
        }

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            
            Item savedItem = itemService.createItem(
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getItemType(),
                item.getUnitOfMeasure(),
                item.getReorderLevel(),
                item.getMinimumStock(),
                item.getMaximumStock(),
                item.getPurchasePrice(),
                item.getSellingPrice(),
                currentUser
            );

            redirectAttributes.addFlashAttribute("successMessage", 
                "Item '" + savedItem.getName() + "' created successfully!");
            return "redirect:/inventory/items/" + savedItem.getId();

        } catch (Exception e) {
            log.error("Error creating item", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error creating item: " + e.getMessage());
            return "redirect:/inventory/items/new";
        }
    }

    @GetMapping("/{id}")
    public String viewItem(@PathVariable Long id, Model model) {
        log.debug("Viewing item with ID: {}", id);

        Optional<Item> itemOpt = itemService.findById(id);
        if (itemOpt.isEmpty()) {
            return "redirect:/inventory/items?error=Item not found";
        }

        model.addAttribute("item", itemOpt.get());
        return "inventory/items/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        log.debug("Showing edit form for item ID: {}", id);

        Optional<Item> itemOpt = itemService.findById(id);
        if (itemOpt.isEmpty()) {
            return "redirect:/inventory/items?error=Item not found";
        }

        model.addAttribute("item", itemOpt.get());
        model.addAttribute("itemTypes", Item.ItemType.values());
        return "inventory/items/form";
    }

    @PostMapping("/{id}/edit")
    public String updateItem(@PathVariable Long id,
                           @Valid @ModelAttribute("item") Item item,
                           BindingResult result,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        log.debug("Updating item with ID: {}", id);

        if (result.hasErrors()) {
            model.addAttribute("itemTypes", Item.ItemType.values());
            return "inventory/items/form";
        }

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            
            Item updatedItem = itemService.updateItem(
                id,
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getItemType(),
                item.getUnitOfMeasure(),
                item.getReorderLevel(),
                item.getMinimumStock(),
                item.getMaximumStock(),
                item.getPurchasePrice(),
                item.getSellingPrice(),
                currentUser
            );

            redirectAttributes.addFlashAttribute("successMessage", 
                "Item '" + updatedItem.getName() + "' updated successfully!");
            return "redirect:/inventory/items/" + updatedItem.getId();

        } catch (Exception e) {
            log.error("Error updating item", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error updating item: " + e.getMessage());
            return "redirect:/inventory/items/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/activate")
    public String activateItem(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        log.debug("Activating item with ID: {}", id);

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            Item activatedItem = itemService.activateItem(id, currentUser);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Item '" + activatedItem.getName() + "' activated successfully!");
        } catch (Exception e) {
            log.error("Error activating item", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error activating item: " + e.getMessage());
        }

        return "redirect:/inventory/items/" + id;
    }

    @PostMapping("/{id}/deactivate")
    public String deactivateItem(@PathVariable Long id,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        log.debug("Deactivating item with ID: {}", id);

        try {
            User currentUser = userService.findByUsername(authentication.getName());
            Item deactivatedItem = itemService.deactivateItem(id, currentUser);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Item '" + deactivatedItem.getName() + "' deactivated successfully!");
        } catch (Exception e) {
            log.error("Error deactivating item", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error deactivating item: " + e.getMessage());
        }

        return "redirect:/inventory/items/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        log.debug("Deleting item with ID: {}", id);

        try {
            Optional<Item> itemOpt = itemService.findById(id);
            String itemName = itemOpt.map(Item::getName).orElse("Unknown");
            
            itemService.deleteItem(id);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Item '" + itemName + "' deleted successfully!");
        } catch (Exception e) {
            log.error("Error deleting item", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error deleting item: " + e.getMessage());
        }

        return "redirect:/inventory/items";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Item> searchItems(@RequestParam String query) {
        log.debug("Searching items with query: {}", query);
        return itemService.findByNameContaining(query);
    }

    @GetMapping("/check-code")
    @ResponseBody
    public boolean checkItemCode(@RequestParam String itemCode) {
        return itemService.isItemCodeUnique(itemCode);
    }

    @GetMapping("/check-barcode")
    @ResponseBody
    public boolean checkBarcode(@RequestParam String barcode) {
        return itemService.isBarcodeUnique(barcode);
    }
}