package com.coopang.order.infrastructure.message;

import com.coopang.apidata.application.order.enums.OrderStatusEnum;

import java.util.UUID;

public interface MessageSender {
    void sendProcessPayment(UUID orderId, String message);
//    void updateOrderStatus(UUID orderId, OrderStatusEnum orderStatus);
//    void updateOrderInfo(UUID orderId, UUID hubId, UUID nearHubId);
}
