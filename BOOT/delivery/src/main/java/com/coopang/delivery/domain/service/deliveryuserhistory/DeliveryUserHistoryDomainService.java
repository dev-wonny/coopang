package com.coopang.delivery.domain.service.deliveryuserhistory;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.domain.entity.deliveryuserhistory.DeliveryUserHistoryEntity;
import com.coopang.delivery.infrastructure.repository.deliveryuserhistory.DeliveryUserHistoryJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "DeliveryUserHistoryDomainService")
@Service
@Transactional
public class DeliveryUserHistoryDomainService {

    private final DeliveryUserHistoryJpaRepository deliveryUserHistoryJpaRepository;

    public DeliveryUserHistoryDomainService(DeliveryUserHistoryJpaRepository deliveryUserHistoryJpaRepository) {
        this.deliveryUserHistoryJpaRepository = deliveryUserHistoryJpaRepository;
    }

    public void createUserHistory(
            UUID deliveryId,
            UUID departureHubId,
            String zipCode,
            String address1,
            String address2,
            UUID userShipperId,
            DeliveryStatusEnum deliveryRouteHistoryStatus
    ) {
        DeliveryUserHistoryEntity deliveryUserHistoryEntity = DeliveryUserHistoryEntity.create(
                deliveryId,
                departureHubId,
                zipCode,
                address1,
                address2,
                userShipperId,
                deliveryRouteHistoryStatus
        );

        deliveryUserHistoryJpaRepository.save(deliveryUserHistoryEntity);
    }
}
