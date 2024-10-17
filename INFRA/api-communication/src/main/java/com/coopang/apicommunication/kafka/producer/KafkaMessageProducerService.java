package com.coopang.apicommunication.kafka.producer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "KafkaMessageService")
@Service
public class KafkaMessageProducerService implements MessageProducer{

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageProducerService(
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic,message);
    }
}