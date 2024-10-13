package com.coopang.product.application.kafka;

import com.coopang.apicommunication.kafka.message.CompleteProduct;
import com.coopang.apicommunication.kafka.message.ProcessProduct;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProductKafkaService {

    //주문 생성 시 재고 차감 요청 응답 메소드
    @Transactional
    public void listenerReduceProductStock(ProcessProduct processProduct) {

        int amount = processProduct.getOrderQuantity();
        UUID productId = processProduct.getProductId();
        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {
            //재고 수량 감소
            productStockEntity.decreaseStock(amount);

            //재고 기록 등록
            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,processProduct.getOrderId(), ProductStockHistoryChangeType.DECREASE,
                    amount,previousStock,productStockEntity.getProductStock().getValue(),"Decrease");

            //재고 기록과 재고와 연결
            productStockEntity.addStockHistory(stockHistory);
            productStockJpaRepository.save(productStockEntity);

            try {

                ProductEntity productEntity = findByProductId(productId);
                //재고수량 차감 완료 메시지 생성 및 주문 서버에 send
                CompleteProduct completeProduct = new CompleteProduct();
                completeProduct.setOrderId(processProduct.getOrderId());
                completeProduct.setCompanyId(productEntity.getCompanyId());
                completeProduct.setOrderTotalPrice(processProduct.getOrderTotalPrice());
                completeProduct.setOrderQuantity(processProduct.getOrderQuantity());
                String completedMessage = objectMapper.writeValueAsString(completeProduct);
                kafkaTemplate.send("complete_product",completedMessage);
                log.info("complete_product kafka sent " + completedMessage);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }

        }
        catch (IllegalArgumentException e)
        {
            //재고 수량 부족 시 주문 서버에 error product 메시지 요청
            log.error(e.getMessage());
            sendOutOfStockMessage(processProduct.getOrderId());
            throw new IllegalArgumentException("주문 수량보다 재고 수량이 부족합니다.");
        }
    }

}
