package com.coopang.product.application.service;

import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.application.service.productstockhistory.ProductStockHistoryService;
import com.coopang.product.application.service.productstock.ProductStockService;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductWithStockAndHistoryService {
    private final ProductService productService;
    private final ProductStockService productStockService;
    private final ProductStockHistoryService productStockHistoryService;

    public ProductWithStockAndHistoryService(ProductService productService, ProductStockService productStockService,
        ProductStockHistoryService productStockHistoryService) {
        this.productService = productService;
        this.productStockService = productStockService;
        this.productStockHistoryService = productStockHistoryService;
    }

    //상품, 상품 재고, 상품 재고 기록을 같이 생성 - 하나의 트랜잭션으로 관리
    public ProductResponseDto createProductWithProductStockAndProductStockHistory(ProductDto productDto) {
        ProductEntity productEntity = productService.createProduct(productDto);

        final int stock = productDto.getProductStock();

        ProductStockEntity productStockEntity = productStockService.createProductStock(productEntity, stock);
        ProductStockHistoryEntity productStockHistoryEntity = productStockHistoryService.create(productStockEntity,stock);

        //4. 연관관계 설정
        productEntity.addProductStockEntity(productStockEntity);
        productStockEntity.addStockHistory(productStockHistoryEntity);

        return ProductResponseDto.of(productEntity);
    }

}
