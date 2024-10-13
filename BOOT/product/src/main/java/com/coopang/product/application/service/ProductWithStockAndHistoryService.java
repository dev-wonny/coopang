package com.coopang.product.application.service;

import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.application.service.productStockHistory.ProductStockHistoryService;
import com.coopang.product.application.service.productstock.ProductStockService;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    public ProductResponseDto createProductWithProductStockAndProductStockHistory(ProductDto productDto) {
        ProductEntity productEntity = productService.createProduct(productDto);

        int stock = productDto.getProductStock();
        ProductStockEntity productStockEntity = productStockService.createProductStock(productEntity, stock);

        ProductStockHistoryEntity productStockHistoryEntity = productStockHistoryService.create(productStockEntity,stock);

        //4. 연관관계 설정
        productEntity.addProductStockEntity(productStockEntity);
        productStockEntity.addStockHistory(productStockHistoryEntity);

        return ProductResponseDto.of(productEntity);
    }

//    // 상품 인포 가져오는 것하고 상품 재고 가져오는거 분리 -> 쿼리 2개로
//    public ProductResponseDto getProductById(UUID productId) {
//        productService.getProductById(productId);// 애는 정보니까 캐시 가능
//        productStockService.getProductStockId(productId); // 애는 캐시 되면 안됨
//        return null;
//    }
}
