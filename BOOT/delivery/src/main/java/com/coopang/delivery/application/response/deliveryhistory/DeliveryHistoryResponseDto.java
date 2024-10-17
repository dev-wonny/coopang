package com.coopang.delivery.application.response.deliveryhistory;

import com.coopang.apidata.application.delivery.enums.DeliveryStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class DeliveryHistoryResponseDto {
    private UUID deliveryId;
    private UUID departureHubId;
    private UUID destinationHubId;
    private String zipCode;
    private String address1;
    private String address2;
    private DeliveryStatusEnum deliveryStatus;
    private LocalDateTime createdAt;
    private UUID shipperId;

    public DeliveryHistoryResponseDto(
            UUID deliveryId,
            UUID departureHubId,
            UUID destinationHubId,
            String zipCode,
            String address1,
            String address2,
            DeliveryStatusEnum deliveryStatus,
            LocalDateTime createdAt,
            UUID shipperId
    ) {
        this.deliveryId = deliveryId;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.deliveryStatus = deliveryStatus;
        this.createdAt = createdAt;
        this.shipperId = shipperId;
    }

}
