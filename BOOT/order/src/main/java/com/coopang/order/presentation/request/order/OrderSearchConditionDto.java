package com.coopang.order.presentation.request.order;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class OrderSearchConditionDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID orderId;
}
