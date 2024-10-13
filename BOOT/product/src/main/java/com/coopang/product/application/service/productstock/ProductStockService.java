package com.coopang.product.application.service.productstock;

import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.service.productStock.ProductStockDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductStockDomainService productStockDomainService;

    public ProductStockEntity createProductStock(ProductEntity productEntity,int productStock) {

        ProductStockEntity productStockEntity = productStockDomainService.create(productStock,productEntity);
        return productStockEntity;
    }
}
