package com.coopang.order.application.service.message;

import com.coopang.order.apicommnication.MessageConsumer;
import com.coopang.order.apicommnication.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
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

    @KafkaListener(topics = "complete_payment",groupId = "order_server")
    public void consumeCompletePayment(String message) {
        paymentHistoryMessageService.processMessage("complete_payment",message);
    }
    @KafkaListener(topics = "cancel_payment",groupId = "order_server")
    public void consumeCancelPayment(String message) {
        paymentHistoryMessageService.processMessage("cancel_payment",message);
    }

    @KafkaListener(topics = "test",groupId = "test")
    public void consumeTest(String message) {
        testMessageService.processMessage("test",message);
    }
}
