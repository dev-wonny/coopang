package com.coopang.product.application.service.productStockHistory;

import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.domain.service.productStockHistory.ProductStockHistoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockHistoryService {

    private final ProductStockHistoryDomainService productStockHistoryDomainService;

    public ProductStockHistoryEntity create(ProductStockEntity productStockEntity, int stock) {

        ProductStockHistoryEntity productStockHistoryEntity = productStockHistoryDomainService.create(productStockEntity,stock);
        return productStockHistoryEntity;
    }
}
