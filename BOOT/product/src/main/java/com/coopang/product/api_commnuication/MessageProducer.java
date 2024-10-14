package com.coopang.product.api_commnuication;

public interface MessageProducer {
    void send(String topic, String message);
}
