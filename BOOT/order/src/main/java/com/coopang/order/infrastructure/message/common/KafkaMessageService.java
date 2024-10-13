package com.coopang.order.infrastructure.message.common;

import com.coopang.apicommunication.kafka.message.ErrorProduct;
import com.coopang.order.application.service.common.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j(topic = "KafkaMessageService")
public class KafkaMessageService implements MessageSender{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    public KafkaMessageService(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            MessageService messageService
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.messageService = messageService;
    }

    @Override
    public void send(String topic, String message) {
        kafkaTemplate.send(
                MessageBuilder.withPayload(message)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build()
        );
    }

    // complete_product 수신
    @KafkaListener(topics = "complete_product", groupId = "order_server")
    public void listenCompleteProduct(String message,
                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic // 토픽 이름을 헤더로 받기
    ) {
        messageService.listen(topic,message);
    }

    // error_product 수신
    @KafkaListener(topics = "error_product", groupId = "order_server")
    public void listenProductError(String message) {
        try {
            ErrorProduct productError = objectMapper.readValue(message, ErrorProduct.class);
            log.info("Product error received: {}", productError.getErrorMessage());
        } catch (Exception e) {
            log.error("Error while processing listenProductError: {}", e.getMessage());
            throw new RuntimeException(e);// 예외 처리
        }
    }


}
