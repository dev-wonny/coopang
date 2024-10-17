package com.coopang.delivery.domain.entity.delivery;


import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.apidata.jpa.entity.address.AddressEntity;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class DeliveryEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "delivery_id", columnDefinition = "UUID", nullable = false, unique = true)
    private UUID deliveryId;

    @Column(name = "order_id", columnDefinition = "UUID", nullable = false)
    private UUID orderId;

    @Column(name = "departure_hub_id", columnDefinition = "UUID", nullable = false)
    private UUID departureHubId;

    @Column(name = "destination_hub_id", columnDefinition = "UUID", nullable = false)
    private UUID destinationHubId;

    @Embedded
    private AddressEntity addressEntity;

    @Column(name = "hub_shipper_id", columnDefinition = "UUID")
    private UUID hubShipperId;

    @Column(name = "user_shipper_id", columnDefinition = "UUID")
    private UUID userShipperId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatusEnum deliveryStatus;

    @Builder
    private DeliveryEntity(
            UUID orderId,
            UUID departureHubId,
            UUID destinationHubId,
            AddressEntity addressEntity,
            UUID hubShipperId,
            UUID userShipperId,
            DeliveryStatusEnum deliveryStatus
    ) {
        this.orderId = orderId;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.addressEntity = addressEntity;
        this.hubShipperId = hubShipperId;
        this.userShipperId = userShipperId;
        this.deliveryStatus = deliveryStatus;
    }

    public static DeliveryEntity create(
            UUID orderId,
            UUID departureHubId,
            UUID destinationHubId,
            String zipCode,
            String address1,
            String address2,
            UUID hubShipperId
    ) {
        return DeliveryEntity.builder()
                .orderId(orderId)
                .departureHubId(departureHubId)
                .destinationHubId(destinationHubId)
                .addressEntity(AddressEntity.create(zipCode, address1, address2))
                .deliveryStatus(DeliveryStatusEnum.HUB_DELIVERY_ASSIGNMENT_IN_PROGRESS)
                .hubShipperId(hubShipperId)
                .build();
    }

    public void setDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setHubShipperId(UUID hubShipperId) {
        this.hubShipperId = hubShipperId;
    }
}
