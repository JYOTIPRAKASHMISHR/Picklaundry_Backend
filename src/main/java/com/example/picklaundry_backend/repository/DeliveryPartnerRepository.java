package com.example.picklaundry_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.picklaundry_backend.entity.DeliveryPartner;

public interface DeliveryPartnerRepository
        extends JpaRepository<DeliveryPartner, Long> {
                Optional<DeliveryPartner> findByUser_Mobile(String mobile);
                
}
