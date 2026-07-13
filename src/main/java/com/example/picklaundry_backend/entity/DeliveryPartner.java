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

     @Column(nullable = false)
        private Integer completedOrders = 0;

        @Column(nullable = false)
        private Double totalEarnings = 0.0;

        @Column(nullable = false)
        private Double pendingEarnings = 0.0;
    }
