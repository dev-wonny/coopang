package com.coopang.ainoti.application.kafka;

import com.coopang.apicommunication.kafka.message.ProcessProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackMessageConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "low_stock_notification", groupId = "message-server-group")
    public void handleProductStockLowNotificationEvent(String message)
    {
//        try {
//            //메시지 변환 String -> class
//            //LowStockNotification lowStockNotification = objectMapper.readValue(message, LowStockNotification.class);
//
//
//        }catch (JsonProcessingException e)
//        {
//            log.error(e.getMessage());
//        }
    }
}
