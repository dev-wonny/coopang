package com.coopang.order.presentation.request.order;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderGetAllConditionDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
