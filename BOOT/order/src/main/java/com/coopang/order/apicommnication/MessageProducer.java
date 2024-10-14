package com.coopang.order.apicommnication;

public interface MessageProducer {
    void send(String topic, String message);
}
