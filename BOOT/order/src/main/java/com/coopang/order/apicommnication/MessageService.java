package com.coopang.order.apicommnication;

public interface MessageService {
    void processMessage(String topic, String Message);
}
