package com.coopang.order.apicommnication;

public interface MessageProducer {
    void sendMessage(String topic, String message);
}
