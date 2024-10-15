package com.coopang.ainoti.application.service.message.slack;

import com.coopang.ainoti.apicommnuication.MessageProducer;
import com.coopang.ainoti.apicommnuication.MessageService;
import com.coopang.ainoti.application.service.SlackNotificationService;
import com.coopang.apicommunication.kafka.message.LowStockNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class SlackMessageService implements MessageService {

    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;
    private final SlackNotificationService slackNotificationService;

    public SlackMessageService(MessageProducer messageProducer, ObjectMapper objectMapper, SlackNotificationService slackNotificationService) {
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
        this.slackNotificationService = slackNotificationService;
    }

    @Override
    public void processMessage(String topic, String message) {
        log.info("Processing Slack message for topic: {}, message: {}", topic, message);

        switch (topic)
        {
            case "low_stock_notification":
                handleLowStockNotification(message);
                break;
            default:
                log.warn("Unknown topic: {}", topic);
                throw new IllegalArgumentException("Unknown topic: " + topic); // 에러 처리 추가
        }
    }

    //재고 부족 시 관리자에게 DM
    private void handleLowStockNotification(String message) {
        try{
            LowStockNotification lowStockNotification = objectMapper.readValue(message, LowStockNotification.class);

            slackNotificationService.listenerSendSlackMessage(lowStockNotification);
        }catch (JsonProcessingException e) {
            log.error("Error while processing low stock notification", e);
        }
    }


    //1. 메세지 보내기
    private void sendMessage(String topic, String message) {
        try {
            messageProducer.sendMessage(topic, message);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}: {}", topic, e.getMessage(), e);
        }
    }
}
