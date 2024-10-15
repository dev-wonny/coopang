package com.coopang.product.application.service.productstock;

import com.coopang.product.application.request.productstock.ProductStockDto;
import com.coopang.product.application.response.productStock.ProductStockResponseDto;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import com.coopang.product.domain.repository.productstock.ProductStockRepository;
import com.coopang.product.domain.service.productstock.ProductStockDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductStockDomainService productStockDomainService;
    private final ProductStockRepository productStockRepository;

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

    //상품의 재고 수량을 감소하는 함수 - 비관적 락 사용
    @Transactional
    public int reduceProductStock(UUID productId, ProductStockDto productStockDto) {
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

        return productStockEntity.getProductStock().getValue();
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
