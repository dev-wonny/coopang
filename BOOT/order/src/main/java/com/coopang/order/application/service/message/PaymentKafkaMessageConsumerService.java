package com.coopang.order.application.service.message;

import com.coopang.apicommunication.kafka.consumer.MessageConsumer;
import com.coopang.apicommunication.kafka.consumer.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j(topic = "PaymentKafkaMessageConsumerService")
public class PaymentKafkaMessageConsumerService implements MessageConsumer {

    private final MessageService paymentHistoryMessageService;

    public PaymentKafkaMessageConsumerService(
            @Qualifier("paymentHistoryMessageService") MessageService paymentHistoryMessageService
    ) {
        this.paymentHistoryMessageService = paymentHistoryMessageService;
    }

    @KafkaListener(topics = "complete_payment",groupId = "order_server")
    public void consumeCompletePayment(String message) {
        paymentHistoryMessageService.processMessage("complete_payment",message);
    }
    @KafkaListener(topics = "cancel_payment",groupId = "order_server")
    public void consumeCancelPayment(String message) {
        paymentHistoryMessageService.processMessage("cancel_payment",message);
    }
}
