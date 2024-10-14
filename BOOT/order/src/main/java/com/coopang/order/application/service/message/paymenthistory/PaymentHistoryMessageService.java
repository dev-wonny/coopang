package com.coopang.order.application.service.message.paymenthistory;

import com.coopang.order.apicommnication.message.CancelPayment;
import com.coopang.order.apicommnication.message.CompletePayment;
import com.coopang.order.apicommnication.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "PaymentHistoryMessageService")
@Service
@Transactional
public class PaymentHistoryMessageService implements MessageService {

    private final ObjectMapper objectMapper;

    public PaymentHistoryMessageService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void listen(String topic, String message) {
        switch (topic) {
            case "complete_payment":
                listenCompletePayment(message);
                break;
            case "cancel_payment":
                listenCancelPayment(message);
                break;
            default:
                log.warn("Unknown topic: {}", topic);
        }
    }

    private void listenCompletePayment(String message) {
        try {
            CompletePayment completePayment = objectMapper.readValue(message, CompletePayment.class);
            log.info("Received complete payment: {}", completePayment);
        } catch (Exception e) {
            log.error("Error while processing listenCompletePayment: {}", e.getMessage(), e);
            throw new RuntimeException(e); // 예외 처리
        }
    }

    private void listenCancelPayment(String message) {
        try {
            CancelPayment cancelPayment = objectMapper.readValue(message, CancelPayment.class);
            log.info("Received cancel payment: {}", cancelPayment);
        } catch (Exception e) {
            log.error("Error while processing listenCancelPayment: {}", e.getMessage(), e);
            throw new RuntimeException(e); // 예외 처리
        }
    }
}
