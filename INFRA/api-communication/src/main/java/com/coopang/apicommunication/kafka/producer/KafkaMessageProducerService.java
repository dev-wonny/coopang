package com.coopang.apicommunication.kafka.producer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Slf4j(topic = "KafkaMessageService")
@Service
@ConditionalOnProperty(name = "message.broker", havingValue = "kafka", matchIfMissing = true)
public class KafkaMessageProducerService implements MessageProducer{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final int maxAttempts;
    private final long backoffMillis;

    public KafkaMessageProducerService(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${message.retry.max-attempts:3}") int maxAttempts,
            @Value("${message.retry.backoff-ms:200}") long backoffMillis
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.maxAttempts = maxAttempts;
        this.backoffMillis = backoffMillis;
    }

    @Override
    public void sendMessage(String topic, String message) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                kafkaTemplate.send(topic, message).get();
                log.info("Kafka message sent successfully. topic={}, attempt={}", topic, attempt);
                return;
            } catch (Exception e) {
                log.warn("Kafka send failed. topic={}, attempt={}/{}", topic, attempt, maxAttempts, e);
                if (attempt == maxAttempts) {
                    throw new IllegalStateException("Kafka send failed after retries.", e);
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
            throw new IllegalStateException("Kafka retry backoff interrupted.", e);
        }
    }
}
