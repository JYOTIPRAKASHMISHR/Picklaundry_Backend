package com.example.picklaundry_backend.repository;

import com.example.picklaundry_backend.entity.LaundryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ServiceRepository
        extends JpaRepository<LaundryService, Long> {

    // Find service by name
    Optional<LaundryService> findByServiceName(String serviceName);

    // Check duplicate service
    boolean existsByServiceName(String serviceName);

    // Get only active services
    List<LaundryService> findByActiveTrue();

}