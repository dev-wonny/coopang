package com.coopang.order.application.service.message;


import com.coopang.order.apicommnication.MessageProducer;
import com.coopang.order.apicommnication.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "OrderMessageService")
@Service
@Transactional
public class TestMessageService implements MessageService {

    private final MessageProducer messageProducer;

    public TestMessageService(
            MessageProducer messageProducer
    ) {
        this.messageProducer = messageProducer;
    }

    @Override
    public void listen(String topic, String message) {
        if (topic.equals("test")) {
            listenTest(message);
        } else {
            log.warn("Unknown topic: {}", topic);
        }
    }

    private void listenTest(String message) {
        try {
            log.info("Test complete received: {}", message);
        } catch (Exception e) {
            log.error("Error while processing Test: {}", e.getMessage(), e);
            throw new RuntimeException(e); // 예외 처리
        }
    }

    public void Test(){
        final String message= "kafka test complete";
        sendMessage("test",message);
    }

    //1. 메세지 보내기
    private void sendMessage(String topic, String message) {
        try {
            messageProducer.send(topic, message);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}: {}", topic, e.getMessage(), e);
        }
    }
}
