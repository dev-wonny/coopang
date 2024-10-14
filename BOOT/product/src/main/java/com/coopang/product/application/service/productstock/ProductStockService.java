package com.coopang.product.application.service.productstock;

import com.coopang.apicommunication.kafka.message.CompleteProduct;
import com.coopang.apicommunication.kafka.message.ErrorProduct;
import com.coopang.apicommunication.kafka.message.ProcessProduct;
import com.coopang.product.application.request.productStock.ProductStockDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.response.productStock.ProductStockResponseDto;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.domain.repository.productStock.ProductStockRepository;
import com.coopang.product.domain.service.productStock.ProductStockDomainService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductService productService;
    private final ProductStockDomainService productStockDomainService;
    private final ProductStockRepository productStockRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String,String> kafkaTemplate;


    //상품 재고 생성
    public ProductStockEntity createProductStock(ProductEntity productEntity,int productStock) {

        ProductStockEntity productStockEntity = productStockDomainService.create(productStock,productEntity);
        return productStockEntity;
    }

    //특정 상품의 재고를 조회
    @Transactional(readOnly = true)
    public ProductStockResponseDto getProductStockByProductId(UUID productId) {
        ProductStockEntity productStockEntity = productStockRepository.findByProductEntity_ProductId(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 재고 입니다.")
        );

        return ProductStockResponseDto.of(productStockEntity);
    }

    //상품의 재고 수량을 증가시키는 함수
    @Transactional
    public void addProductStock(UUID productId, ProductStockDto productStockDto) {

        int amount = productStockDto.getAmount();

        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productStockRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {
            productStockEntity.increaseStock(amount);
            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,productStockDto.getOrderId(), ProductStockHistoryChangeType.INCREASE,
                amount,previousStock,productStockEntity.getProductStock().getValue(),"Increase");

            productStockEntity.addStockHistory(stockHistory);
        }catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            throw new IllegalArgumentException("재고의 수량은 음수가 될 수 없습니다.");
        }

    }

    //상품의 재고 수량을 감소하는 함수 - 비관적 락 사
    @Transactional
    public void reduceProductStock(UUID productId, ProductStockDto productStockDto) {
        int amount = productStockDto.getAmount();
        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productStockRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {
            //재고 수량 감소
            productStockEntity.decreaseStock(amount);

            //재고 기록 등록
            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,productStockDto.getOrderId(), ProductStockHistoryChangeType.DECREASE,
                amount,previousStock,productStockEntity.getProductStock().getValue(),"Decrease");

            //재고 기록과 재고와 연결
            productStockEntity.addStockHistory(stockHistory);

        }
        catch (IllegalArgumentException e)
        {
            //재고 수량 부족 시 주문 서버에 error product 메시지 요청
            log.error(e.getMessage());
            throw new IllegalArgumentException("주문 수량보다 재고 수량이 부족합니다.");
        }
    }

    //주문 생성 시 재고 차감 요청 응답 메소드
    @Transactional
    public void listenerReduceProductStock(ProcessProduct processProduct) {

        int amount = processProduct.getOrderQuantity();
        UUID productId = processProduct.getProductId();
        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productStockRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {
            //재고 수량 감소
            productStockEntity.decreaseStock(amount);

            //재고 기록 등록
            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,processProduct.getOrderId(), ProductStockHistoryChangeType.DECREASE,
                amount,previousStock,productStockEntity.getProductStock().getValue(),"Decrease");

            //재고 기록과 재고와 연결
            productStockEntity.addStockHistory(stockHistory);

            try {

                ProductResponseDto productResponseDto = productService.getProductById(productId);
                //재고수량 차감 완료 메시지 생성 및 주문 서버에 send
                CompleteProduct completeProduct = new CompleteProduct();
                completeProduct.setOrderId(processProduct.getOrderId());
                completeProduct.setCompanyId(productResponseDto.getCompanyId());
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


    //주문 수량이 재고 수량보다 많을 경우 에러메시지 주문서버에 전달
    private void sendOutOfStockMessage(UUID orderId)
    {
        try{
            ErrorProduct errorProduct = new ErrorProduct();
            errorProduct.setOrderId(orderId);
            errorProduct.setErrorMessage("재고수량이 부족합니다.");
            String errorMessage = objectMapper.writeValueAsString(errorProduct);
            kafkaTemplate.send("error_product",errorMessage);

        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 잔액 부족/ 결제실패에 대한 상품 수량 롤백
     */
    @Transactional
    public void rollbackProduct(UUID orderId,UUID productId,int orderQuantity){

        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productStockRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {

            productStockEntity.increaseStock(orderQuantity);

            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,orderId, ProductStockHistoryChangeType.INCREASE,
                orderQuantity,previousStock,productStockEntity.getProductStock().getValue(),"Rollback Product");

            productStockEntity.addStockHistory(stockHistory);
        }catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            throw new IllegalArgumentException("재고의 수량은 음수가 될 수 없습니다.");
        }
    }

    /**
     * 결제 취소 시 상품 수량 복구
     * @param orderId
     * @param productId
     * @param orderQuantity
     */
    @Transactional
    public void cancelProduct(UUID orderId, UUID productId, int orderQuantity)
    {

        //비관적 락을 이용하여 조회 -> 다른 트랜잭션은 읽기와 쓰기 불가능
        ProductStockEntity productStockEntity = productStockRepository.findAndLockProductStock(productId);

        int previousStock = productStockEntity.getProductStock().getValue();

        try {

            productStockEntity.increaseStock(orderQuantity);

            ProductStockHistoryEntity stockHistory = ProductStockHistoryEntity.create(null,productStockEntity,orderId, ProductStockHistoryChangeType.INCREASE,
                orderQuantity,previousStock,productStockEntity.getProductStock().getValue(),"Cancel Order and Product Stock increase");

            productStockEntity.addStockHistory(stockHistory);

        }catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            throw new IllegalArgumentException("재고의 수량은 음수가 될 수 없습니다.");
        }

    }
}
