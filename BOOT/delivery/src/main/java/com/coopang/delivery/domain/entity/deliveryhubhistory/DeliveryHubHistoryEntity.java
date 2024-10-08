package com.coopang.delivery.domain.entity.deliveryhubhistory;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_delivery_hub_route_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class DeliveryHubHistoryEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "delivery_hub_route_history_id", columnDefinition = "UUID")
    private UUID deliveryHubRouteHistoryId;


    @Column(name = "delivery_id", columnDefinition = "UUID")
    private UUID deliveryId;

    @Column(name = "departure_hub_id", columnDefinition = "UUID", nullable = false)
    private UUID departureHubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_route_history_status", nullable = false)
    private DeliveryStatusEnum deliveryRouteHistoryStatus;

    @Column(name = "arrival_hub_id", columnDefinition = "UUID")
    private UUID arrivalHubId;

    @Column(name = "hub_shipper_id", columnDefinition = "UUID")
    private UUID hubShipperId;

    @Builder
    private DeliveryHubHistoryEntity(
            UUID deliveryHubRouteHistoryId,
            UUID deliveryId,
            UUID departureHubId,
            UUID arrivalHubId,
            UUID hubShipperId,
            DeliveryStatusEnum deliveryRouteHistoryStatus
    ){
        this.deliveryHubRouteHistoryId = deliveryHubRouteHistoryId;
        this.deliveryId = deliveryId;
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.hubShipperId = hubShipperId;
        this.deliveryRouteHistoryStatus = deliveryRouteHistoryStatus;
    }

    public static DeliveryHubHistoryEntity create(
            UUID deliveryId,
            UUID departureHubId,
            UUID arrivalHubId,
            UUID hubShipperId,
            DeliveryStatusEnum deliveryRouteHistoryStatus
    ){
        return DeliveryHubHistoryEntity.builder()
                .deliveryId(deliveryId)
                .departureHubId(departureHubId)
                .arrivalHubId(arrivalHubId)
                .hubShipperId(hubShipperId)
                .deliveryRouteHistoryStatus(deliveryRouteHistoryStatus)
                .build();
    }
}
