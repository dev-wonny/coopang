package com.coopang.product.application.service.productstock;

import com.coopang.product.application.response.productStock.ProductStockResponseDto;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.repository.productStock.ProductStockRepository;
import com.coopang.product.domain.service.productStock.ProductStockDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        ProductStockEntity productStockEntity = productStockRepository.findByProductId(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 재고 입니다.")
        );

        return ProductStockResponseDto.of(productStockEntity);
    }
}
