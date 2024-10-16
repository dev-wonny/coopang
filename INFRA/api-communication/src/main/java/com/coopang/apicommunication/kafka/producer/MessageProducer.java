package com.coopang.apicommunication.kafka.producer;

public interface MessageProducer {
    void sendMessage(String topic, String message);
}
