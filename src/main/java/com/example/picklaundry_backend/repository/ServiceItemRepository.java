package com.example.picklaundry_backend.repository;

import com.example.picklaundry_backend.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceItemRepository
        extends JpaRepository<ServiceItem, Long> {

    // All items of one service
    List<ServiceItem> findByServiceId(Long serviceId);
    

    // Active items of one service
    List<ServiceItem> findByServiceIdAndActiveTrue(Long serviceId);

    // Find item by name inside a service
    List<ServiceItem> findByServiceIdAndItemNameContainingIgnoreCase(
            Long serviceId,
            String itemName
    );

}