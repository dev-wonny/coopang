package com.coopang.apicommunication.rabbit.producer;

import com.coopang.apicommunication.kafka.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j(topic = "RabbitMessageService")
@Service
@ConditionalOnProperty(name = "message.broker", havingValue = "rabbit")
public class RabbitMessageProducerService implements MessageProducer {

    private final RabbitTemplate rabbitTemplate;
    private final int maxAttempts;
    private final long backoffMillis;

    public RabbitMessageProducerService(
            RabbitTemplate rabbitTemplate,
            @Value("${message.retry.max-attempts:3}") int maxAttempts,
            @Value("${message.retry.backoff-ms:200}") long backoffMillis
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.maxAttempts = maxAttempts;
        this.backoffMillis = backoffMillis;
    }

    @Override
    public void sendMessage(String topic, String message) {
        // RabbitMQ 기본 exchange에 routingKey(topic)로 메시지를 발행한다.
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                rabbitTemplate.convertAndSend("", topic, message);
                log.info("RabbitMQ message sent successfully. topic={}, attempt={}", topic, attempt);
                return;
            } catch (Exception e) {
                log.warn("RabbitMQ send failed. topic={}, attempt={}/{}", topic, attempt, maxAttempts, e);
                if (attempt == maxAttempts) {
                    throw new IllegalStateException("RabbitMQ send failed after retries.", e);
                }
                sleepBackoff();
            }
        }
    }

    private void sleepBackoff() {
        try {
            Thread.sleep(backoffMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("RabbitMQ retry backoff interrupted.", e);
        }
        rabbitTemplate.convertAndSend("", topic, message);
    }
}
