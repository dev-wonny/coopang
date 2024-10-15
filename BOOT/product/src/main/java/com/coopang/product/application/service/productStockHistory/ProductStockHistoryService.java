package com.coopang.product.application.service.productStockHistory;

import com.coopang.product.application.request.Productstockhistory.ProductStockHistoryDto;
import com.coopang.product.application.response.ProductStockHistory.ProductStockHistoryResponseDto;
import com.coopang.product.application.response.productStock.ProductStockResponseDto;
import com.coopang.product.application.service.productstock.ProductStockService;
import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import com.coopang.product.domain.repository.productstockhistory.ProductStockHistoryRepository;
import com.coopang.product.domain.service.productstockhistory.ProductStockHistoryDomainService;
import com.coopang.product.presentation.request.productstockhistory.ProductStockHistorySearchConditionDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductStockHistoryService {

    private final ProductStockHistoryDomainService productStockHistoryDomainService;
    private final ProductStockHistoryRepository productStockHistoryRepository;
    private final ProductStockService productStockService;

    //재고 기록 생성
    public ProductStockHistoryEntity create(ProductStockEntity productStockEntity, int stock) {

        ProductStockHistoryEntity productStockHistoryEntity = productStockHistoryDomainService.create(productStockEntity,stock);
        return productStockHistoryEntity;
    }

    //재고기록 논리적으로 삭제하는 함수
    @Transactional
    public void deleteProductStockHistory(UUID productId, UUID productStockHistoryId) {

        ProductStockResponseDto productStockResponseDto = productStockService.getProductStockByProductId(productId);


        ProductStockHistoryEntity productStockHistoryEntity = findStockHistoryById(
            productStockHistoryId, productStockResponseDto.getProductStockId());

        productStockHistoryEntity.setDeleted(true);

    }

    //특정 상품의 재고 기록을 변경하는 함수
    @Transactional
    public void updateProductStockHistory(ProductStockHistoryDto productStockHistoryDto,UUID productId, UUID productStockHistoryId) {

        ProductStockResponseDto productStockResponseDto = productStockService.getProductStockByProductId(productId);

        ProductStockHistoryEntity productStockHistoryEntity = findStockHistoryById(
            productStockHistoryId, productStockResponseDto.getProductStockId());

        productStockHistoryEntity.updateProductStockHistory(
            productStockHistoryDto.getProductStockHistoryChangeQuantity(),
            productStockHistoryDto.getProductStockHistoryPreviousQuantity(),
            productStockHistoryDto.getProductStockHistoryCurrentQuantity(),
            productStockHistoryDto.getProductStockHistoryAdditionalInfo(),
            productStockHistoryDto.getProductStockHistoryChangeType()
        );
    }

    //특정 상품의 재고기록들을 모두 조회하는 함수 (페이징, 정렬기능 지원)
    @Transactional(readOnly = true)
    public Page<ProductStockHistoryResponseDto> getStockHistoriesByProductId(UUID productId, Pageable pageable) {

        return productStockHistoryRepository.getProductStockHistoryByProductId(productId, pageable).map(ProductStockHistoryResponseDto::of);

    }

    //특정상품의 재고기록들을 키워드 혹은 날짜별로 조회하는 함수
    @Transactional(readOnly = true)
    public Page<ProductStockHistoryResponseDto> getStockHistoriesByProductIdWithCondition(
        ProductStockHistorySearchConditionDto condition, UUID productId, Pageable pageable) {

        return productStockHistoryRepository.searchProductStockHistoryByProductId(condition,productId,pageable).map(ProductStockHistoryResponseDto::of);
    }

    //특정 상품의 재고 기록 엔티티를 찾는 함수
    private ProductStockHistoryEntity findStockHistoryById(UUID productStockHistoryId,
        UUID productStockId) {
        return productStockHistoryRepository.findByProductStockHistoryIdAndProductStockEntity_ProductStockId(productStockHistoryId,productStockId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 재고기록입니다."));
    }
}
