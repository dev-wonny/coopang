package com.coopang.delivery.application.service;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.domain.service.DeliveryHubHistoryDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "DeliveryHubhHistoryService")
@Service
@Transactional
public class DeliveryHubHistoryService {

    private final DeliveryHubHistoryDomainService deliveryHubHistoryDomainService;

    public DeliveryHubHistoryService(DeliveryHubHistoryDomainService deliveryHubHistoryDomainService) {
        this.deliveryHubHistoryDomainService = deliveryHubHistoryDomainService;
    }
    // 배송 기록 등록
    public void createHubHistory(
            UUID deliveryId,
            UUID departureHubId,
            UUID arrivalHubId,
            UUID hubShipperId,
            DeliveryStatusEnum deliveryRouteHistoryStatus
    ){
        deliveryHubHistoryDomainService.createHubHistory(
                deliveryId,
                departureHubId,
                arrivalHubId,
                hubShipperId,
                deliveryRouteHistoryStatus
        );
    }

}
