package com.coopang.delivery.application.response.delivery;

import com.coopang.apidata.application.address.Address;
import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
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

    private DeliveryResponseDto(
            UUID deliveryId,
            UUID orderId,
            UUID departureHubId,
            UUID destinationHubId,
            Address address,
            UUID hubShipperId,
            UUID userShipperId,
            DeliveryStatusEnum deliveryStatus,
            boolean isDeleted
    ){
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
        return new DeliveryResponseDto(
                delivery.getDeliveryId(),
                delivery.getOrderId(),
                delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                new Address(delivery.getAddressEntity().getZipCode(),delivery.getAddressEntity().getAddress1(), delivery.getAddressEntity().getAddress2()),
                delivery.getHubShipperId(),
                delivery.getUserShipperId(),
                delivery.getDeliveryStatus(),
                delivery.isDeleted()
        );
    }
}
