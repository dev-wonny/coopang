package com.coopang.delivery.domain.repository;

import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.presentation.request.DeliverySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Optional<DeliveryEntity> findById(UUID deliveryId);

    Optional<DeliveryEntity> findByOrderId(UUID orderId);

    Optional<DeliveryEntity> findByIdHubId(UUID deliveryId, UUID hubId);

    Optional<DeliveryEntity> findByOrderIdHubId(UUID orderId, UUID hubId);

    Page<DeliveryEntity> findAll(Pageable pageable);

    Page<DeliveryEntity> findAllHubId(Pageable pageable,UUID hubId);

    Page<DeliveryEntity> search(DeliverySearchCondition condition, Pageable pageable);

    List<DeliveryEntity> findAllByDestinationHubIdAndHubShipperId(UUID destinationHubId, UUID hubSipperId);

//    List<DeliveryEntity> findAllByDeliveryStatus(DeliveryStatusEnum deliveryStatus);
}
