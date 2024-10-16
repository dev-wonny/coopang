package com.coopang.delivery.apicommnication;

public interface MessageService {
    void processMessage(String topic, String message);
}
