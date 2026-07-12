package com.example.picklaundry_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPartnerResponse {

    private Long id;

    private String fullName;

    private String mobile;

    private Integer completedOrders;

    private Integer pendingEarnings;

    private Integer totalEarnings;

}