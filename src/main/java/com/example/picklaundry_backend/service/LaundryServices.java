package com.example.picklaundry_backend.service;

import com.example.picklaundry_backend.dto.AddItemRequest;
import com.example.picklaundry_backend.dto.AddServiceRequest;
import com.example.picklaundry_backend.dto.ServiceResponse;
import com.example.picklaundry_backend.dto.UpdateItemRequest;
import com.example.picklaundry_backend.entity.ServiceItem;

import java.util.List;

public interface LaundryServices {

    String addService(AddServiceRequest request);

    String addItem(AddItemRequest request);

    String updateItem(UpdateItemRequest request);

    String deleteItem(Long itemId);

    List<ServiceResponse> getAllServices();

    ServiceResponse getService(Long serviceId);

    
    List<ServiceItem> getItems(Long serviceId);

    String deleteService(Long serviceId);

    String changeServiceStatus(Long serviceId, boolean active);

    String changeItemStatus(Long itemId, boolean active);
}