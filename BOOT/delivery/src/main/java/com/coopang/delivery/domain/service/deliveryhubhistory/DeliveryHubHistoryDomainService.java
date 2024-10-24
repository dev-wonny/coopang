package com.coopang.delivery.domain.service.deliveryhubhistory;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;
import com.coopang.delivery.infrastructure.repository.deliveryhubhistory.DeliveryHubHistoryJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "DeliveryHubHistoryDomainService")
@Service
@Transactional
public class DeliveryHubHistoryDomainService {

    private final DeliveryHubHistoryJpaRepository deliveryHubHistoryJpaRepository;

    public DeliveryHubHistoryDomainService(DeliveryHubHistoryJpaRepository deliveryHubHistoryJpaRepository) {
        this.deliveryHubHistoryJpaRepository = deliveryHubHistoryJpaRepository;
    }

    public void createHubHistory(
            UUID deliveryId
            , UUID departureHubId
            , UUID arrivalHubId
            , UUID hubShipperId
            , DeliveryStatusEnum deliveryRouteHistoryStatus
    ) {
        DeliveryHubHistoryEntity deliveryHubHistoryEntity = DeliveryHubHistoryEntity.create(
                deliveryId
                , departureHubId
                , arrivalHubId
                , hubShipperId
                , deliveryRouteHistoryStatus
        );

        deliveryHubHistoryJpaRepository.save(deliveryHubHistoryEntity);
    }
}
