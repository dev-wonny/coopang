package com.coopang.product.application.kafka;

import com.coopang.apicommunication.kafka.message.ProcessProduct;
import com.coopang.apicommunication.kafka.message.RollbackProduct;
import com.coopang.product.application.request.productStock.ProductStockDto;
import com.coopang.product.application.service.product.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductConsumer {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    //consumer는 group id필수라 일단 서버 이름으로 생성
    @KafkaListener(topics = "process_product", groupId = "product-server-group")
    public void handleProductStockReduceEvent(String message)
     {
        log.info("process_product consumer start");
        try {
            //메시지 변환 String -> class
            ProcessProduct processProduct = objectMapper.readValue(message, ProcessProduct.class);

            //재고 감소 요청
            productService.listenerReduceProductStock(processProduct);

        }catch (JsonProcessingException e)
        {
            log.error(e.getMessage());
        }
    }

    //consumer는 group id필수라 일단 서버 이름으로 생성
    @KafkaListener(topics = "rollback_product", groupId = "rollback-product-group")
    public void handleProductStockRollbackEvent(String message)
    {

        try {
            //메시지 변환 String -> class
            RollbackProduct rollbackProduct = objectMapper.readValue(message, RollbackProduct.class);

            //재고 증가 요청
            productService.rollbackProduct(rollbackProduct.getOrderId(),rollbackProduct.getProductId(),
                rollbackProduct.getOrderQuantity());

        }catch (JsonProcessingException e)
        {
            log.error(e.getMessage());
        }
    }
}
