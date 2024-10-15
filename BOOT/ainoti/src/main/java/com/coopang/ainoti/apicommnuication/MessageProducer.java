package com.coopang.ainoti.apicommnuication;

public interface MessageProducer {
    void sendMessage(String topic, String message);
}
