package com.coopang.product.application.service.message;

import com.coopang.apicommunication.kafka.consumer.MessageConsumer;
import com.coopang.product.application.service.message.product.ProductMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class KafkaProductMessageConsumerService implements MessageConsumer {

    private final ProductMessageService productMessageService;

    public KafkaProductMessageConsumerService(ProductMessageService productMessageService) {
        this.productMessageService = productMessageService;
    }

    @KafkaListener(topics = "process_product", groupId = "product-server-group")
    public void consumeProcessProduct(String message)
    {
        log.info("process product message: {}", message);
        productMessageService.processMessage("process_product",message);
    }

    @KafkaListener(topics = "rollback_product", groupId = "rollback-product-group")
    public void consumeRollbackProduct(String message)
    {
        log.info("rollback product message: {}", message);
        productMessageService.processMessage("rollback_product",message);
    }
}
