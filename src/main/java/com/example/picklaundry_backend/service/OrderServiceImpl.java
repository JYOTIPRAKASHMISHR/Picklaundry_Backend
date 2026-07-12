package com.example.picklaundry_backend.service;

import com.example.picklaundry_backend.dto.OrderItemRequest;
import com.example.picklaundry_backend.dto.OrderItemResponse;
import com.example.picklaundry_backend.dto.OrderRequest;
import com.example.picklaundry_backend.dto.OrderResponse;
import com.example.picklaundry_backend.entity.DeliveryPartner;
import com.example.picklaundry_backend.entity.Order;
import com.example.picklaundry_backend.entity.OrderItem;
import com.example.picklaundry_backend.entity.ServiceItem;
import com.example.picklaundry_backend.entity.User;
import com.example.picklaundry_backend.enums.OrderStatus;
import com.example.picklaundry_backend.enums.PaymentStatus;
import com.example.picklaundry_backend.repository.DeliveryPartnerRepository;
import com.example.picklaundry_backend.repository.OrderItemRepository;
import com.example.picklaundry_backend.repository.OrderRepository;
import com.example.picklaundry_backend.repository.ServiceItemRepository;
import com.example.picklaundry_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final UserRepository userRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ServiceItemRepository serviceItemRepository,
            UserRepository userRepository,
            DeliveryPartnerRepository deliveryPartnerRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.serviceItemRepository = serviceItemRepository;
        this.userRepository = userRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
    }

    @Override
    public OrderResponse placeOrder(String username, OrderRequest request) {

        System.out.println("Logged in mobile : " + username);

        User user = userRepository.findByMobile(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();

        order.setOrderId(generateOrderId());
        order.setUser(user);
        order.setCustomerName(user.getFullName());
        order.setMobileNumber(user.getMobile());
        order.setPickupAddress(request.getPickupAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setSpecialInstruction(request.getSpecialInstruction());
        order.setPickupDate(request.getPickupDate());
        order.setSelectedPlan(request.getSelectedPlan());
        order.setStatus(OrderStatus.PICKUP_PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        int totalItems = 0;

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        // ===============================
        // Calculate Total
        // ===============================

        for (OrderItemRequest dto : request.getItems()) {

            ServiceItem item = serviceItemRepository.findById(dto.getServiceItemId())
                    .orElseThrow(() -> new RuntimeException("Service Item not found"));

            BigDecimal price = getPrice(item, request.getSelectedPlan());

            BigDecimal subtotal =
                    price.multiply(BigDecimal.valueOf(dto.getQuantity()));

            total = total.add(subtotal);
            totalItems += dto.getQuantity();
        }

        order.setTotalItems(totalItems);
        order.setTotalAmount(total);
        order.setGrandTotal(total);

        order = orderRepository.save(order);

        // ===============================
        // Save Order Items
        // ===============================

        for (OrderItemRequest dto : request.getItems()) {

            ServiceItem item = serviceItemRepository.findById(dto.getServiceItemId())
                    .orElseThrow(() -> new RuntimeException("Service Item not found"));

            BigDecimal price = getPrice(item, request.getSelectedPlan());

            BigDecimal subtotal =
                    price.multiply(BigDecimal.valueOf(dto.getQuantity()));

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setService(item.getService());
            orderItem.setServiceItem(item);

            orderItem.setServiceName(item.getService().getServiceName());
            orderItem.setItemName(item.getItemName());

            orderItem.setSelectedPlan(request.getSelectedPlan());
            orderItem.setQuantity(dto.getQuantity());

            orderItem.setPrice(price);
            orderItem.setSubtotal(subtotal);

            orderItemRepository.save(orderItem);

            itemResponses.add(
                    OrderItemResponse.builder()
                            .itemId(item.getId())
                            .serviceName(item.getService().getServiceName())
                            .itemName(item.getItemName())
                            .selectedPlan(request.getSelectedPlan())
                            .quantity(dto.getQuantity())
                            .price(price)
                            .subtotal(subtotal)
                            .build()
            );
        }

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .mobileNumber(order.getMobileNumber())
                .pickupAddress(order.getPickupAddress())
                .selectedPlan(order.getSelectedPlan())
                .totalItems(order.getTotalItems())
                .totalAmount(order.getTotalAmount())
                .grandTotal(order.getGrandTotal())
                .orderStatus(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }

    // ======================================
    // Get Price According To Selected Plan
    // ======================================

    private BigDecimal getPrice(ServiceItem item, String plan) {

        switch (plan.toUpperCase()) {

            case "BASE":
                return item.getBasePrice();

            case "EXPRESS":
                return item.getExpressPrice();

            case "PREMIUM":
                return item.getPremiumPrice();

            default:
                throw new RuntimeException("Invalid Plan");
        }
    }

    // ======================================
    // My Orders
    // ======================================

    @Override
public List<OrderResponse> getMyOrders(String username) {

    User user = userRepository.findByMobile(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Order> orders =
            orderRepository.findByUserOrderByCreatedAtDesc(user);

    return orders.stream().map(order -> {

        List<OrderItem> orderItems =
                orderItemRepository.findByOrder(order);

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .mobileNumber(order.getMobileNumber())
                .pickupAddress(order.getPickupAddress())
                .selectedPlan(order.getSelectedPlan())
                .totalItems(order.getTotalItems())
                .totalAmount(order.getTotalAmount())
                .grandTotal(order.getGrandTotal())
                .orderStatus(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .deliveryPartnerName(
                        order.getDeliveryPartner() != null
                                ? order.getDeliveryPartner()
                                        .getUser()
                                        .getFullName()
                                : null
                        )

                        .deliveryPartnerMobile(
                                order.getDeliveryPartner() != null
                                        ? order.getDeliveryPartner()
                                                .getUser()
                                                .getMobile()
                                        : null
                        )
                .createdAt(order.getCreatedAt())
                .items(orderItems.stream().map(item ->
                        com.example.picklaundry_backend.dto.OrderItemResponse.builder()
                                .itemId(item.getServiceItem().getId())
                                .serviceName(item.getServiceName())
                                .itemName(item.getItemName())
                                .selectedPlan(item.getSelectedPlan())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .subtotal(item.getSubtotal())
                                .build()
                ).toList())
                .build();

    }).toList();
}

    // ======================================
    // Order Details
    // ======================================

   @Override
public OrderResponse getOrderDetails(String username, String orderId) {

    User user = userRepository.findByMobile(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Order order = orderRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (!order.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Unauthorized");
    }

    List<OrderItem> orderItems =
            orderItemRepository.findByOrder(order);

    return OrderResponse.builder()
            .orderId(order.getOrderId())
            .customerName(order.getCustomerName())
            .mobileNumber(order.getMobileNumber())
            .pickupAddress(order.getPickupAddress())
            .selectedPlan(order.getSelectedPlan())
            .totalItems(order.getTotalItems())
            .totalAmount(order.getTotalAmount())
            .grandTotal(order.getGrandTotal())
            .orderStatus(order.getStatus().name())
            .paymentStatus(order.getPaymentStatus().name())
            .deliveryPartnerName(
        order.getDeliveryPartner() != null
                ? order.getDeliveryPartner()
                        .getUser()
                        .getFullName()
                : null
)

.deliveryPartnerMobile(
        order.getDeliveryPartner() != null
                ? order.getDeliveryPartner()
                        .getUser()
                        .getMobile()
                : null
)
            .createdAt(order.getCreatedAt())
            .items(orderItems.stream().map(item ->
                    com.example.picklaundry_backend.dto.OrderItemResponse.builder()
                            .itemId(item.getServiceItem().getId())
                            .serviceName(item.getServiceName())
                            .itemName(item.getItemName())
                            .selectedPlan(item.getSelectedPlan())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .subtotal(item.getSubtotal())
                            .build()
            ).toList())
            .build();
}
    // ======================================
    // Cancel Order
    // ======================================

   @Override
public void cancelOrder(String username, String orderId) {

    User user = userRepository.findByMobile(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Order order = orderRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    if (!order.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Unauthorized");
    }

    order.setStatus(OrderStatus.CANCELLED);

    orderRepository.save(order);
}

    // ======================================
    // Generate Order ID
    // ======================================

    private String generateOrderId() {

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String random = UUID.randomUUID()
                .toString()
                .substring(0, 5)
                .toUpperCase();

        return "ORD" + date + random;
    }

    public List<OrderResponse> getPickupOrders() {

    List<Order> orders =
            orderRepository.findByStatus(OrderStatus.PICKUP_PENDING);

    return orders.stream().map(order -> {

        List<OrderItem> orderItems =
                orderItemRepository.findByOrder(order);

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .mobileNumber(order.getMobileNumber())
                .pickupAddress(order.getPickupAddress())
                .selectedPlan(order.getSelectedPlan())
                .totalItems(order.getTotalItems())
                .totalAmount(order.getTotalAmount())
                .grandTotal(order.getGrandTotal())
                .orderStatus(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .createdAt(order.getCreatedAt())
                .items(orderItems.stream().map(item ->
                        OrderItemResponse.builder()
                                .itemId(item.getServiceItem().getId())
                                .serviceName(item.getServiceName())
                                .itemName(item.getItemName())
                                .selectedPlan(item.getSelectedPlan())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .subtotal(item.getSubtotal())
                                .build()
                ).toList())
                .build();

    }).toList();
}
private List<OrderResponse> convertOrders(List<Order> orders) {

    return orders.stream().map(order -> {

        List<OrderItem> orderItems =
                orderItemRepository.findByOrder(order);

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerName(order.getCustomerName())
                .mobileNumber(order.getMobileNumber())
                .pickupAddress(order.getPickupAddress())
                .selectedPlan(order.getSelectedPlan())
                .totalItems(order.getTotalItems())
                .totalAmount(order.getTotalAmount())
                .grandTotal(order.getGrandTotal())
                .orderStatus(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .deliveryPartnerName(
        order.getDeliveryPartner() != null
                ? order.getDeliveryPartner()
                        .getUser()
                        .getFullName()
                : null
)

.deliveryPartnerMobile(
        order.getDeliveryPartner() != null
                ? order.getDeliveryPartner()
                        .getUser()
                        .getMobile()
                : null
)
                .createdAt(order.getCreatedAt())
                .items(orderItems.stream().map(item ->
                        OrderItemResponse.builder()
                                .itemId(item.getServiceItem().getId())
                                .serviceName(item.getServiceName())
                                .itemName(item.getItemName())
                                .selectedPlan(item.getSelectedPlan())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .subtotal(item.getSubtotal())
                                .build()
                ).toList())
                .build();

    }).toList();
}

public void pickupOrder(String orderId){

    Order order = orderRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus(OrderStatus.PICKED_UP);

    orderRepository.save(order);

}

public void processing(String orderId){

    Order order = orderRepository.findByOrderId(orderId)
            .orElseThrow();

    order.setStatus(OrderStatus.PROCESSING);

    orderRepository.save(order);

}

public void readyForDelivery(String orderId){

    Order order = orderRepository.findByOrderId(orderId)
            .orElseThrow();

    order.setStatus(OrderStatus.READY_FOR_DELIVERY);

    orderRepository.save(order);

}
public List<OrderResponse> getDeliveryOrders() {

    List<Order> orders =
        orderRepository.findByStatusIn(
            List.of(
                OrderStatus.READY_FOR_DELIVERY,
                OrderStatus.OUT_FOR_DELIVERY
            )
        );

    return convertOrders(orders);
}
@Override
public void outForDelivery(String orderId, String username) {

    Order order = orderRepository
            .findByOrderId(orderId)
            .orElseThrow(() ->
                    new RuntimeException("Order Not Found"));

    DeliveryPartner partner =
            deliveryPartnerRepository
                    .findByUser_Mobile(username)
                    .orElseThrow(() ->
                            new RuntimeException("Delivery Partner Not Found"));

    order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

    order.setDeliveryPartner(partner);

    orderRepository.save(order);
}
@Override
public void delivered(String orderId, String username) {

    Order order = orderRepository
            .findByOrderId(orderId)
            .orElseThrow(() ->
                    new RuntimeException("Order Not Found"));

    DeliveryPartner partner =
            deliveryPartnerRepository
                    .findByUser_Mobile(username)
                    .orElseThrow(() ->
                            new RuntimeException("Delivery Partner Not Found"));

    partner.setCompletedOrders(
            partner.getCompletedOrders() + 1);

    partner.setPendingEarnings(
            partner.getPendingEarnings() + 40);

    partner.setTotalEarnings(
            partner.getTotalEarnings() + 40);

    deliveryPartnerRepository.save(partner);

    order.setStatus(OrderStatus.DELIVERED);

    order.setDeliveryPartner(partner);

    order.setPaymentStatus(PaymentStatus.PAID);

    orderRepository.save(order);
}
@Override
public List<OrderResponse> getProcessingOrders() {

    List<Order> pickedUpOrders =
            orderRepository.findByStatus(OrderStatus.PICKED_UP);

    List<Order> processingOrders =
            orderRepository.findByStatus(OrderStatus.PROCESSING);

    pickedUpOrders.addAll(processingOrders);

    return convertOrders(pickedUpOrders);
}

@Override
public long getOrderCount() {
    return orderRepository.count();
}

@Override
public BigDecimal getTotalRevenue() {
   return orderRepository.getRevenueByStatus(OrderStatus.DELIVERED);
}

@Override
public long getDeliveredOrderCount(){

    return orderRepository.countByStatus(
            OrderStatus.DELIVERED
    );
}

@Override
public long getPickupPendingCount(){

    return orderRepository.countByStatus(
            OrderStatus.PICKUP_PENDING
    );
}

@Override
public List<OrderResponse> getAllOrders() {

    List<Order> orders =
            orderRepository.findAll();

    return convertOrders(orders);
}

@Override
public Integer getPendingEarnings(String username){

    DeliveryPartner partner =
            deliveryPartnerRepository
                    .findByUser_Mobile(username)
                    .orElseThrow();

    return partner.getPendingEarnings();
}
@Override
public Integer getTotalEarnings(String username){

    DeliveryPartner partner =
            deliveryPartnerRepository
                    .findByUser_Mobile(username)
                    .orElseThrow();

    return partner.getTotalEarnings();
}
@Override
public Integer getCompletedOrders(String username){

    DeliveryPartner partner =
            deliveryPartnerRepository
                    .findByUser_Mobile(username)
                    .orElseThrow();

    return partner.getCompletedOrders();
}
}