package com.example.picklaundry_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.picklaundry_backend.service.DeliveryPartnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final DeliveryPartnerService deliveryPartnerService;

    @GetMapping("/delivery-partners")
    public ResponseEntity<?> getPartners() {

        return ResponseEntity.ok(
                deliveryPartnerService.getAllPartners());

    }

    @PutMapping("/partner/{id}/mark-paid")
    public ResponseEntity<String> markPaid(
            @PathVariable Long id) {

        deliveryPartnerService.markPartnerPaid(id);

        return ResponseEntity.ok("Payment Updated");

    }
}