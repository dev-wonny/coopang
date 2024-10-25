package com.coopang.delivery.domain.entity.deliveryuserhistory;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.apidata.jpa.entity.address.AddressEntity;
import com.coopang.apidata.jpa.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "p_delivery_user_route_histories")
@Getter
@NoArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class DeliveryUserHistoryEntity extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "delivery_User_route_history_id", columnDefinition = "UUID")
    private UUID deliveryUserRouteHistoryId;


    @Column(name = "delivery_id", columnDefinition = "UUID")
    private UUID deliveryId;

    @Column(name = "departure_hub_id", columnDefinition = "UUID", nullable = false)
    private UUID departureHubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_route_history_status", nullable = false)
    private DeliveryStatusEnum deliveryRouteHistoryStatus;

    @Embedded
    private AddressEntity addressEntity;

    @Column(name = "user_shipper_id", columnDefinition = "UUID")
    private UUID userShipperId;

    @Builder
    private DeliveryUserHistoryEntity(
            UUID deliveryUserRouteHistoryId
            , UUID deliveryId
            , UUID departureHubId
            , AddressEntity addressEntity
            , UUID userShipperId
            , DeliveryStatusEnum deliveryRouteHistoryStatus
    ) {
        this.deliveryUserRouteHistoryId = deliveryUserRouteHistoryId;
        this.deliveryId = deliveryId;
        this.departureHubId = departureHubId;
        this.addressEntity = addressEntity;
        this.userShipperId = userShipperId;
        this.deliveryRouteHistoryStatus = deliveryRouteHistoryStatus;
    }

    public static DeliveryUserHistoryEntity create(
            UUID deliveryId
            , UUID departureHubId
            , String zipCode
            , String address1
            , String address2
            , UUID userShipperId
            , DeliveryStatusEnum deliveryRouteHistoryStatus
    ) {
        return DeliveryUserHistoryEntity.builder()
                .deliveryId(deliveryId)
                .departureHubId(departureHubId)
                .addressEntity(AddressEntity.create(zipCode, address1, address2))
                .userShipperId(userShipperId)
                .deliveryRouteHistoryStatus(deliveryRouteHistoryStatus)
                .build();
    }
}
