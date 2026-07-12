package com.example.picklaundry_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.picklaundry_backend.dto.DeliveryPartnerResponse;
import com.example.picklaundry_backend.entity.DeliveryPartner;
import com.example.picklaundry_backend.repository.DeliveryPartnerRepository;

@Service
public class DeliveryPartnerServiceImpl implements DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;

    public DeliveryPartnerServiceImpl(
            DeliveryPartnerRepository deliveryPartnerRepository) {

        this.deliveryPartnerRepository = deliveryPartnerRepository;
    }

    @Override
    public List<DeliveryPartnerResponse> getAllPartners() {

        List<DeliveryPartner> partners =
                deliveryPartnerRepository.findAll();

        return partners.stream().map(partner ->

                DeliveryPartnerResponse.builder()

                        .id(partner.getId())

                        .fullName(partner.getUser().getFullName())

                        .mobile(partner.getUser().getMobile())

                        .completedOrders(partner.getCompletedOrders())

                        .pendingEarnings(partner.getPendingEarnings())

                        .totalEarnings(partner.getTotalEarnings())

                        .build()

        ).toList();

    }

    @Override
    public void markPartnerPaid(Long partnerId) {

        DeliveryPartner partner =
                deliveryPartnerRepository.findById(partnerId)
                        .orElseThrow(() ->
                                new RuntimeException("Partner Not Found"));

        partner.setPendingEarnings(0);

        deliveryPartnerRepository.save(partner);

    }
}