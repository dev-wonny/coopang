package com.coopang.order.infrastructure.message.kafka.order;

import com.coopang.apicommunication.kafka.message.ProcessDelivery;
import com.coopang.apicommunication.kafka.message.ProcessPayment;
import com.coopang.apicommunication.kafka.message.RollbackProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j(topic = "OrderKafkaProducer")
@Component
public class OrderKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderKafkaProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendProcessProduct(String message) {
        kafkaTemplate.send("process_product", message);
        log.info("sendProcessProduct processing message sent: {}", message);
    }

    public void sendProcessDelivery(ProcessDelivery processDelivery) {
        try {
            String message = objectMapper.writeValueAsString(processDelivery);
            kafkaTemplate.send("process_delivery", message);
            log.info("sendProcessDelivery processing message sent: {}", message);
        } catch (Exception e) {
            log.error("Error while sendProcessDelivery: {}", e.getMessage());
        }
    }

    public void sendProcessPayment(ProcessPayment processPayment) {
        try {
            String message = objectMapper.writeValueAsString(processPayment);
            kafkaTemplate.send("process_payment", message);
            log.info("sendProcessPayment processing message sent: {}", message);
        } catch (Exception e) {
            log.error("Error while sendProcessPayment: {}", e.getMessage());
        }
    }

    public void sendRollbackProduct(RollbackProduct rollbackProduct) {
        try {
            String message = objectMapper.writeValueAsString(rollbackProduct);
            kafkaTemplate.send("rollback_product", message);
            log.info("sendRollbackProduct processing message sent: {}", message);
        } catch (Exception e) {
            log.error("Error while sendRollbackProduct: {}", e.getMessage());
        }
    }
}