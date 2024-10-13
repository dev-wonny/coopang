package com.coopang.order.application.service.common;

public interface MessageService {
    void listen(String topic, String Message);
}
