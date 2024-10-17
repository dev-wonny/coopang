package com.coopang.delivery.application.service.deliveryhubhistory;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;
import com.coopang.delivery.domain.repository.deliveryhubhistory.DeliveryHubHistoryRepository;
import com.coopang.delivery.domain.service.deliveryhubhistory.DeliveryHubHistoryDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "DeliveryHubhHistoryService")
@Service
@Transactional
public class DeliveryHubHistoryService {

    private final DeliveryHubHistoryDomainService deliveryHubHistoryDomainService;
    private final DeliveryHubHistoryRepository deliveryHubHistoryRepository;

    public DeliveryHubHistoryService(
            DeliveryHubHistoryDomainService deliveryHubHistoryDomainService,
            DeliveryHubHistoryRepository deliveryHubHistoryRepository
    ) {
        this.deliveryHubHistoryDomainService = deliveryHubHistoryDomainService;
        this.deliveryHubHistoryRepository = deliveryHubHistoryRepository;
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

    public List<DeliveryHubHistoryEntity> getHubHistoryById(UUID deliveryId) {
        return deliveryHubHistoryRepository.findByDeliveryId(deliveryId);
    }

}
