package com.coopang.order.application.service.message;

import com.coopang.apicommunication.kafka.consumer.MessageConsumer;
import com.coopang.apicommunication.kafka.consumer.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j(topic = "OrderKafkaMessageConsumerService")
@Service
public class OrderKafkaMessageConsumerService implements MessageConsumer {

    private final MessageService orderMessageService;

    public OrderKafkaMessageConsumerService(
            @Qualifier("orderMessageService") MessageService orderMessageService
    ) {
        this.orderMessageService = orderMessageService;
    }

    @KafkaListener(topics = "complete_product",groupId = "order_server")
    public void consumeCompleteProduct(String message) {
        orderMessageService.processMessage("complete_product",message);
    }
    @KafkaListener(topics = "error_product",groupId = "order_server")
    public void consumeErrorProduct(String message) {
        orderMessageService.processMessage("error_product",message);
    }
    @KafkaListener(topics = "process_change_status",groupId = "order_server")
    public void consumeProcessChangeStatus(String message) {
        orderMessageService.processMessage("process_change_status",message);
    }
}
