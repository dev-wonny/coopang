package com.coopang.delivery.apicommnication;

public interface MessageProducer {
    void sendMessage(String topic, String message);
}
