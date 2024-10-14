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
    public void listenCompleteProduct(String message) {
        orderMessageService.listen("complete_product",message);
    }
    @KafkaListener(topics = "error_product",groupId = "order_server")
    public void listenErrorProduct(String message) {
        orderMessageService.listen("error_product",message);
    }
    @KafkaListener(topics = "process_change_status",groupId = "order_server")
    public void listenProcessChangeStatus(String message) {
        orderMessageService.listen("process_change_status",message);
    }

    @KafkaListener(topics = "complete_payment",groupId = "order_server")
    public void listenCompletePayment(String message) {
        paymentHistoryMessageService.listen("complete_payment",message);
    }
    @KafkaListener(topics = "cancel_payment",groupId = "order_server")
    public void listenCancelPayment(String message) {
        paymentHistoryMessageService.listen("cancel_payment",message);
    }

    @KafkaListener(topics = "test",groupId = "test")
    public void listenTest(String message) {
        testMessageService.listen("test",message);
    }
}
