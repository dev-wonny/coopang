package com.coopang.delivery.application.response.delivery;

import com.coopang.apidata.application.address.Address;
import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DeliveryResponseDto {
    private UUID deliveryId;
    private UUID orderId;
    private UUID departureHubId;
    private UUID destinationHubId;
    private Address address;
    private UUID hubShipperId;
    private UUID userShipperId;
    private DeliveryStatusEnum deliveryStatus;
    private boolean isDeleted;

    @Builder
    private DeliveryResponseDto(
            UUID deliveryId
            , UUID orderId
            , UUID departureHubId
            , UUID destinationHubId
            , Address address
            , UUID hubShipperId
            , UUID userShipperId
            , DeliveryStatusEnum deliveryStatus
            , boolean isDeleted
    ) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.address = address;
        this.hubShipperId = hubShipperId;
        this.userShipperId = userShipperId;
        this.deliveryStatus = deliveryStatus;
        this.isDeleted = isDeleted;
    }

    public static DeliveryResponseDto fromDelivery(DeliveryEntity delivery) {
        return DeliveryResponseDto
                .builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .departureHubId(delivery.getDepartureHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .address(new Address(
                        delivery.getAddressEntity().getZipCode()
                        , delivery.getAddressEntity().getAddress1()
                        , delivery.getAddressEntity().getAddress2()
                ))
                .hubShipperId(delivery.getHubShipperId())
                .userShipperId(delivery.getUserShipperId())
                .deliveryStatus(delivery.getDeliveryStatus())
                .isDeleted(delivery.isDeleted())
                .build();
    }
}
