package com.example.picklaundry_backend.service;

import com.example.picklaundry_backend.dto.DeliveryPartnerResponse;
import java.util.List;

public interface DeliveryPartnerService {

    List<DeliveryPartnerResponse> getAllPartners();

    void markPartnerPaid(Long partnerId);

}