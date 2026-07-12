    package com.example.picklaundry_backend.entity;


    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Table(name = "delivery_partners")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class DeliveryPartner {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

    

        @OneToOne
        @JoinColumn(name = "user_id")
        private User user;

        private Integer completedOrders = 0;

        private Integer pendingEarnings = 0;

        private Integer totalEarnings = 0;
    }
