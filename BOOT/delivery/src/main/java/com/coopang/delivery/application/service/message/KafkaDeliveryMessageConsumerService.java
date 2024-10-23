package com.coopang.delivery.application.service.message;

import com.coopang.apicommunication.kafka.consumer.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j(topic = "KafkaDeliveryMessageConsumerService")
@Service
public class KafkaDeliveryMessageConsumerService {

    private final MessageService deliveryMessageService;

    public KafkaDeliveryMessageConsumerService(
            @Qualifier("deliveryMessageService") MessageService deliveryMessageService
    ) {
        this.deliveryMessageService = deliveryMessageService;
    }

    @KafkaListener(topics = "complete_delivery", groupId = "delivery_server")
    public void consumeCompleteDelivery(String message) {
        log.info("consume complete delivery message: {}", message);
        deliveryMessageService.processMessage("complete_delivery", message);
    }

    @KafkaListener(topics = "cancel_delivery", groupId = "delivery_server")
    public void consumeCancelDelivery(String message) {
        log.info("consume cancel delivery message: {}", message);
        deliveryMessageService.processMessage("cancel_delivery", message);
    }

}
