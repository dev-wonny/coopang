package com.coopang.delivery.application.service.deliveryuserhistory;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.domain.entity.deliveryuserhistory.DeliveryUserHistoryEntity;
import com.coopang.delivery.domain.repository.deliveryuserhistory.DeliveryUserHistoryRepository;
import com.coopang.delivery.domain.service.deliveryuserhistory.DeliveryUserHistoryDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "DeliveryUserHistoryService")
@Service
@Transactional
public class DeliveryUserHistoryService {

    private final DeliveryUserHistoryDomainService deliveryUserHistoryDomainService;
    private final DeliveryUserHistoryRepository deliveryUserHistoryRepository;

    public DeliveryUserHistoryService(
            DeliveryUserHistoryDomainService deliveryUserHistoryDomainService,
            DeliveryUserHistoryRepository deliveryUserHistoryRepository
    ) {
        this.deliveryUserHistoryDomainService = deliveryUserHistoryDomainService;
        this.deliveryUserHistoryRepository = deliveryUserHistoryRepository;
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
        deliveryUserHistoryDomainService.createUserHistory(
                deliveryId,
                departureHubId,
                zipCode,
                address1,
                address2,
                userShipperId,
                deliveryRouteHistoryStatus
        );
    }

    public List<DeliveryUserHistoryEntity> getHubHistoryById(UUID deliveryId) {
        return deliveryUserHistoryRepository.findByDeliveryId(deliveryId);
    }
}
