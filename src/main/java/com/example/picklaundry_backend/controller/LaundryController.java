package com.example.picklaundry_backend.controller;

import com.example.picklaundry_backend.dto.AddItemRequest;
import com.example.picklaundry_backend.dto.AddServiceRequest;
import com.example.picklaundry_backend.dto.ServiceResponse;
import com.example.picklaundry_backend.dto.UpdateItemRequest;
import com.example.picklaundry_backend.entity.ServiceItem;
import com.example.picklaundry_backend.service.LaundryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LaundryController {

    private final LaundryServices    laundryService;

    // ==========================================
    // Add New Service
    // ==========================================

    @PostMapping
    public ResponseEntity<String> addService(
            @RequestBody AddServiceRequest request) {

        return ResponseEntity.ok(
                laundryService.addService(request)
        );
    }

    // ==========================================
    // Add Item To Service
    // ==========================================

    @PostMapping("/items")
    public ResponseEntity<String> addItem(
            @RequestBody AddItemRequest request) {

        return ResponseEntity.ok(
                laundryService.addItem(request)
        );
    }

    // ==========================================
    // Update Item
    // ==========================================

    @PutMapping("/items")
    public ResponseEntity<String> updateItem(
            @RequestBody UpdateItemRequest request) {

        return ResponseEntity.ok(
                laundryService.updateItem(request)
        );
    }

    // ==========================================
    // Delete Item
    // ==========================================

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<String> deleteItem(
            @PathVariable Long itemId) {

        return ResponseEntity.ok(
                laundryService.deleteItem(itemId)
        );
    }

    // ==========================================
    // Get All Services
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices() {

        return ResponseEntity.ok(
                laundryService.getAllServices()
        );
    }

    // ==========================================
    // Get Single Service
    // ==========================================

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceResponse> getService(
            @PathVariable Long serviceId) {

        return ResponseEntity.ok(
                laundryService.getService(serviceId)
        );
    }


    @GetMapping("/{serviceId}/items")
public ResponseEntity<List<ServiceItem>> getItems(
        @PathVariable Long serviceId) {

    return ResponseEntity.ok(
            laundryService.getItems(serviceId)
    );
}

    // ==========================================
    // Delete Service
    // ==========================================

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<String> deleteService(
            @PathVariable Long serviceId) {

        return ResponseEntity.ok(
                laundryService.deleteService(serviceId)
        );
    }

    // ==========================================
    // Enable / Disable Service
    // ==========================================

    @PutMapping("/{serviceId}/status")
    public ResponseEntity<String> changeServiceStatus(

            @PathVariable Long serviceId,

            @RequestParam boolean active) {

        return ResponseEntity.ok(
                laundryService.changeServiceStatus(
                        serviceId,
                        active
                )
        );
    }

    // ==========================================
    // Enable / Disable Item
    // ==========================================

    @PutMapping("/items/{itemId}/status")
    public ResponseEntity<String> changeItemStatus(

            @PathVariable Long itemId,

            @RequestParam boolean active) {

        return ResponseEntity.ok(
                laundryService.changeItemStatus(
                        itemId,
                        active
                )
        );
    }

}