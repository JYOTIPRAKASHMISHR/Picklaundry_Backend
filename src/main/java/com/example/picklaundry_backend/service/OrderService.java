package com.example.picklaundry_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.jspecify.annotations.Nullable;

import com.example.picklaundry_backend.dto.OrderRequest;
import com.example.picklaundry_backend.dto.OrderResponse;

public interface OrderService {

    /**
     * Place a new order
     *
     * @param username Username extracted from JWT
     * @param request Order request from Flutter
     * @return OrderResponse
     */
    OrderResponse placeOrder(String username, OrderRequest request);

    /**
     * Get all orders of logged-in user
     *
     * @param username Username extracted from JWT
     * @return List<OrderResponse>
     */
    List<OrderResponse> getMyOrders(String username);

    /**
     * Get single order details
     *
     * @param username Username extracted from JWT
     * @param orderId Generated order id (Example: ORD202606260001)
     * @return OrderResponse
     */
    OrderResponse getOrderDetails(String username, String orderId);

    /**
     * Cancel an order
     *
     * @param username Username extracted from JWT
     * @param orderId Generated order id
     */
    void cancelOrder(String username, String orderId);

    @Nullable
    Object getPickupOrders();

    void pickupOrder(String orderId);

    void delivered(String orderId, String username);

    void outForDelivery(String orderId, String username);
    void readyForDelivery(String orderId);


    List<OrderResponse> getDeliveryOrders();

    

    void processing(String orderId);
    List<OrderResponse> getProcessingOrders();

    long getOrderCount();

    BigDecimal getTotalRevenue();
    long getDeliveredOrderCount();
     long getPickupPendingCount();

     List<OrderResponse> getAllOrders();

     Integer getPendingEarnings(String username);

Integer getTotalEarnings(String username);

Integer getCompletedOrders(String username);

}