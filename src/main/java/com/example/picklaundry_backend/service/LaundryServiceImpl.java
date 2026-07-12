package com.example.picklaundry_backend.service;

import com.example.picklaundry_backend.dto.*;
import com.example.picklaundry_backend.entity.LaundryService;
import com.example.picklaundry_backend.entity.ServiceItem;
import com.example.picklaundry_backend.repository.ServiceItemRepository;
import com.example.picklaundry_backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaundryServiceImpl implements LaundryServices {

    private final ServiceRepository serviceRepository;
    private final ServiceItemRepository itemRepository;

    //======================================================
    // Add New Service
    //======================================================

    @Override
    public String addService(AddServiceRequest request) {

        if (serviceRepository.existsByServiceName(request.getServiceName())) {
            throw new RuntimeException("Service Already Exists");
        }

        LaundryService service = LaundryService.builder()
                .serviceName(request.getServiceName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .active(request.isActive())
                .build();

        serviceRepository.save(service);

        return "Service Added Successfully";
    }

    //======================================================
    // Add Item
    //======================================================

    @Override
  
public String addItem(AddItemRequest request) {

    LaundryService service = serviceRepository.findById(request.getServiceId())
            .orElseThrow(() -> new RuntimeException("Service Not Found"));

    ServiceItem item = ServiceItem.builder()
            .itemName(request.getItemName())
            .basePrice(request.getBasePrice())
            .expressPrice(request.getExpressPrice())
            .premiumPrice(request.getPremiumPrice())
            .displayOrder(request.getDisplayOrder())
            .active(request.getActive() != null ? request.getActive() : true)
            .service(service)
            .build();

    itemRepository.save(item);

    return "Item Added Successfully";
}
    //======================================================
    // Update Item
    //======================================================

   @Override
public String updateItem(UpdateItemRequest request) {

    ServiceItem item = itemRepository.findById(request.getItemId())
            .orElseThrow(() -> new RuntimeException("Item Not Found"));

    item.setItemName(request.getItemName());
    item.setBasePrice(request.getBasePrice());
    item.setExpressPrice(request.getExpressPrice());
    item.setPremiumPrice(request.getPremiumPrice());
    item.setDisplayOrder(request.getDisplayOrder());

    item.setActive(
            request.getActive() != null
                    ? request.getActive()
                    : item.isActive());

    itemRepository.save(item);

    return "Item Updated Successfully";
}
    //======================================================
    // Delete Item
    //======================================================

    @Override
    public String deleteItem(Long itemId) {

        itemRepository.deleteById(itemId);

        return "Item Deleted Successfully";
    }

    //======================================================
    // Get All Services
    //======================================================

    @Override
    public List<ServiceResponse> getAllServices() {

        return serviceRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    //======================================================
    // Get Single Service
    //======================================================

    @Override
    public ServiceResponse getService(Long serviceId) {

        LaundryService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service Not Found"));

        return convertToResponse(service);
    }

    //======================================================
    // Delete Service
    //======================================================

    @Override
    public String deleteService(Long serviceId) {

        serviceRepository.deleteById(serviceId);

        return "Service Deleted Successfully";
    }

    //======================================================
    // Enable / Disable Service
    //======================================================

    @Override
    public String changeServiceStatus(Long serviceId, boolean active) {

        LaundryService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service Not Found"));

        service.setActive(active);

        serviceRepository.save(service);

        return "Service Status Updated";
    }

    //======================================================
    // Enable / Disable Item
    //======================================================

    @Override
    public String changeItemStatus(Long itemId, boolean active) {

        ServiceItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item Not Found"));

        item.setActive(active);

        itemRepository.save(item);

        return "Item Status Updated";
    }

    //======================================================
    // Convert Entity -> DTO
    //======================================================

    private ServiceResponse convertToResponse(LaundryService service) {

        return ServiceResponse.builder()

                .serviceId(service.getId())
                .serviceName(service.getServiceName())
                .description(service.getDescription())
                .icon(service.getIcon())
                .active(service.isActive())

                .items(

                        service.getItems()

                                .stream()

                                .map(item -> ServiceResponse.ItemResponse.builder()

                                        .itemId(item.getId())
                                        .itemName(item.getItemName())
                                        .basePrice(item.getBasePrice().toString())
                                        .expressPrice(item.getExpressPrice().toString())
                                        .premiumPrice(item.getPremiumPrice().toString())
                                        .displayOrder(item.getDisplayOrder())
                                        .active(item.isActive())

                                        .build())

                                .collect(Collectors.toList())

                )

                .build();
    }

    @Override
public List<ServiceItem> getItems(Long serviceId) {

    // Check if service exists
    serviceRepository.findById(serviceId)
            .orElseThrow(() ->
                    new RuntimeException("Service not found"));

    // Return all items of the service
    return itemRepository.findByServiceId(serviceId);
}

}