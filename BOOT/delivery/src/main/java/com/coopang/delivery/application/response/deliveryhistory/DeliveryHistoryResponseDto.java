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
    private DeliveryStatusEnum deliveryStatus;
    private LocalDateTime createdAt;

    public DeliveryHistoryResponseDto(
            UUID deliveryId,
            DeliveryStatusEnum deliveryStatus,
            LocalDateTime createdAt
    ) {
        this.deliveryId = deliveryId;
        this.deliveryStatus = deliveryStatus;
        this.createdAt = createdAt;
    }

}
