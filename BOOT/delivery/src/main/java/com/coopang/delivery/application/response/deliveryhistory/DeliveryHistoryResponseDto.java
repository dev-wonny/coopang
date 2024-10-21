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
    private DeliveryStatusEnum deliveryStatus;
    private LocalDateTime createdAt;
    private UUID shipperId;

    public DeliveryHistoryResponseDto(
            UUID deliveryId,
            UUID departureHubId,
            UUID destinationHubId,
            DeliveryStatusEnum deliveryStatus,
            LocalDateTime createdAt,
            UUID shipperId
    ) {
        this.deliveryId = deliveryId;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.deliveryStatus = deliveryStatus;
        this.createdAt = createdAt;
        this.shipperId = shipperId;
    }

}
