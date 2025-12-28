package com.coopang.apicommunication.rabbit.producer;

import com.coopang.apicommunication.kafka.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j(topic = "RabbitMessageService")
@Service
@ConditionalOnProperty(name = "message.broker", havingValue = "rabbit")
public class RabbitMessageProducerService implements MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMessageProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(String topic, String message) {
        // RabbitMQ 기본 exchange에 routingKey(topic)로 메시지를 발행한다.
        rabbitTemplate.convertAndSend("", topic, message);
    }
}
