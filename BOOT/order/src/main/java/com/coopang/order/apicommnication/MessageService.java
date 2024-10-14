package com.coopang.order.apicommnication;

public interface MessageService {
    void listen(String topic, String Message);
}
