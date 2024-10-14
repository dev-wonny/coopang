package com.coopang.order.application.service.message.order;

import com.coopang.order.apicommnication.MessageConsumer;
import com.coopang.order.apicommnication.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaMessageConsumerService implements MessageConsumer {

    private final MessageService orderMessageService;
    private final MessageService testMessageService;

    public KafkaMessageConsumerService(
            @Qualifier("orderMessageService") MessageService orderMessageService,
            @Qualifier("testMessageService") MessageService testMessageService
    ) {
        this.orderMessageService = orderMessageService;
        this.testMessageService = testMessageService;
    }

    @KafkaListener(topics = "complete_product",groupId = "order_server")
    public void listenCompleteProduct(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        orderMessageService.listen(topic,message);
    }

    @KafkaListener(topics = "test",groupId = "test")
    public void listenTest(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        testMessageService.listen(topic,message);
    }
}
