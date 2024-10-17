package com.coopang.apicommunication.kafka.consumer;

public interface MessageService {
    void processMessage(String topic, String message);
}
