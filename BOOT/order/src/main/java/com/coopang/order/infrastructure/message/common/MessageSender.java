package com.coopang.order.infrastructure.message.common;

public interface MessageSender {
    void send(String topic, String message);
}
