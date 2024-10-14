package com.coopang.order.application.service.message;

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
    private final MessageService paymentHistoryMessageService;

    public KafkaMessageConsumerService(
            @Qualifier("orderMessageService") MessageService orderMessageService,
            @Qualifier("testMessageService") MessageService testMessageService,
            @Qualifier("paymentHistoryMessageService") MessageService paymentHistoryMessageService
    ) {
        this.orderMessageService = orderMessageService;
        this.testMessageService = testMessageService;
        this.paymentHistoryMessageService = paymentHistoryMessageService;
    }

    @KafkaListener(topics = "complete_product",groupId = "order_server")
    public void listenCompleteProduct(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        orderMessageService.listen(topic,message);
    }
    @KafkaListener(topics = "error_product",groupId = "order_server")
    public void listenErrorProduct(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        orderMessageService.listen(topic,message);
    }
    @KafkaListener(topics = "process_change_status",groupId = "order_server")
    public void listenProcessChangeStatus(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        orderMessageService.listen(topic,message);
    }

    @KafkaListener(topics = "complete_payment",groupId = "order_server")
    public void listenCompletePayment(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        paymentHistoryMessageService.listen(topic,message);
    }
    @KafkaListener(topics = "cancel_payment",groupId = "order_server")
    public void listenCancelPayment(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        paymentHistoryMessageService.listen(topic,message);
    }

    @KafkaListener(topics = "test",groupId = "test")
    public void listenTest(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        testMessageService.listen(topic,message);
    }
}
