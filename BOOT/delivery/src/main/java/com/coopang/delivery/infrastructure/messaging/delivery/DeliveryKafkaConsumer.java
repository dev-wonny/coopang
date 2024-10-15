package com.coopang.delivery.infrastructure.messaging.delivery;

import com.coopang.apicommunication.kafka.message.CancelDelivery;
import com.coopang.apicommunication.kafka.message.ProcessDelivery;
import com.coopang.delivery.domain.service.delivery.DeliveryDomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final DeliveryDomainService deliveryDomainService;

    public DeliveryKafkaConsumer(
            ObjectMapper objectMapper,
            DeliveryDomainService deliveryDomainService
    ) {
        this.objectMapper = objectMapper;
        this.deliveryDomainService = deliveryDomainService;
    }

    // Todo : process_delivery 수신
    @KafkaListener(topics = "process_delivery", groupId = "delivery-server")
    public void processDelivery(String message) {
            try {
                ProcessDelivery processDelivery = objectMapper.readValue(message, ProcessDelivery.class);
                deliveryDomainService.createProcessDelivery(processDelivery);
            } catch (Exception e){
                e.printStackTrace();
            }

    }
    // Todo : cancel_delivery 수신
    // 배송 취소
    @KafkaListener(topics = "cancel_delivery", groupId = "delivery-server")
    public void cancelDelivery(String message){
        try {
            CancelDelivery cancelDelivery = objectMapper.readValue(message, CancelDelivery.class);
            deliveryDomainService.cancelDelivery(cancelDelivery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
