package com.coopang.order.application.service.order;

import com.coopang.apidata.application.order.enums.OrderStatusEnum;

import java.util.UUID;

public interface IOrderService {
    void updateOrderStatus(UUID orderId, OrderStatusEnum orderStatus);
    void updateOrderInfo(UUID orderId, UUID hubId, UUID nearHubId);
}
