package com.example.picklaundry_backend.controller;

import com.example.picklaundry_backend.dto.OrderRequest;
import com.example.picklaundry_backend.dto.OrderResponse;
import com.example.picklaundry_backend.entity.User;
import com.example.picklaundry_backend.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    // ==========================
    // PLACE ORDER
    // ==========================

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request,
            Principal principal) {

                System.out.println("========== PLACE ORDER ==========");
    System.out.println(request);

        System.out.println("Logged in User : " + principal.getName());

        OrderResponse response =
                orderService.placeOrder(
                        principal.getName(),
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================
    // GET MY ORDERS
    // ==========================

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            Principal principal) {

                System.out.println("========== GET MY ORDERS ==========");
                Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();

System.out.println(authentication);

         System.out.println("Principal = " + principal);

    if (principal != null) {
        System.out.println("Principal Name = " + principal.getName());
    }

        return ResponseEntity.ok(
                orderService.getMyOrders(
                        principal.getName()));
    }

    // ==========================
    // GET ORDER DETAILS
    // ==========================

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(
            @PathVariable String orderId,
            Principal principal) {

        System.out.println("Logged in User : " + principal.getName());

        return ResponseEntity.ok(
                orderService.getOrderDetails(
                        principal.getName(),
                        orderId));
    }
@PutMapping("/{orderId}/pickup")
public ResponseEntity<String> pickupOrder(
        @PathVariable String orderId){

    orderService.pickupOrder(orderId);

    return ResponseEntity.ok("Order Picked Successfully");

}

@PutMapping("/{orderId}/processing")
public ResponseEntity<String> processingOrder(
        @PathVariable String orderId) {

    orderService.processing(orderId);

    return ResponseEntity.ok("Order moved to Processing");
}

@GetMapping("/processing-orders")
public ResponseEntity<?> getProcessingOrders() {

    return ResponseEntity.ok(
            orderService.getProcessingOrders()
    );

}
@PutMapping("/{orderId}/ready")
public ResponseEntity<String> readyForDelivery(
        @PathVariable String orderId) {

    orderService.readyForDelivery(orderId);

    return ResponseEntity.ok("Order is Ready For Delivery");
}
@GetMapping("/delivery-orders")
public ResponseEntity<@Nullable Object> getDeliveryOrders() {

    return ResponseEntity.ok(
            orderService.getDeliveryOrders()
    );
}
@PutMapping("/{orderId}/out-for-delivery")
public ResponseEntity<String> outForDelivery(
        @PathVariable String orderId,
        Principal principal) {

    orderService.outForDelivery(
            orderId,
            principal.getName());

    return ResponseEntity.ok("Order is Out For Delivery");
}
@PutMapping("/{orderId}/delivered")
public ResponseEntity<String> delivered(
        @PathVariable String orderId,
        Principal principal) {

    orderService.delivered(
            orderId,
            principal.getName());

    return ResponseEntity.ok("Order Delivered Successfully");
}
    // ==========================
    // CANCEL ORDER
    // ==========================

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable String orderId,
            Principal principal) {

        System.out.println("Logged in User : " + principal.getName());

        orderService.cancelOrder(
                principal.getName(),
                orderId);

        return ResponseEntity.ok(
                "Order cancelled successfully");
    }

    @GetMapping("/pickup-pending")
public ResponseEntity<?> getPickupOrders() {

    return ResponseEntity.ok(
            orderService.getPickupOrders()
    );

}


@GetMapping("/order-count")
public ResponseEntity<Long> getOrderCount() {

    return ResponseEntity.ok(
            orderService.getOrderCount()
    );

}

@GetMapping("/total-revenue")
public ResponseEntity<BigDecimal> getTotalRevenue() {

    return ResponseEntity.ok(
            orderService.getTotalRevenue()
    );
}

@GetMapping("/delivered-count")
public ResponseEntity<Long> getDeliveredOrderCount(){

    return ResponseEntity.ok(
            orderService.getDeliveredOrderCount()
    );
}

@GetMapping("/pickup-pending-count")
public ResponseEntity<Long> getPickupPendingCount(){

    return ResponseEntity.ok(
            orderService.getPickupPendingCount()
    );
}

@GetMapping("/all")
public List<OrderResponse> getAllOrders() {
    return orderService.getAllOrders();
}

@GetMapping("/partner/pending-earnings")
public ResponseEntity<Integer> getPendingEarnings(
        Principal principal){

    return ResponseEntity.ok(
            orderService.getPendingEarnings(
                    principal.getName()));
}

@GetMapping("/partner/total-earnings")
public ResponseEntity<Integer> getTotalEarnings(
        Principal principal){

    return ResponseEntity.ok(
            orderService.getTotalEarnings(
                    principal.getName()));
}
@GetMapping("/partner/completed-orders")
public ResponseEntity<Integer> getCompletedOrders(
        Principal principal){

    return ResponseEntity.ok(
            orderService.getCompletedOrders(
                    principal.getName()));
}

}