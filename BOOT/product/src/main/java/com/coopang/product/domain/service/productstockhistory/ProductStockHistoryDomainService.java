package com.coopang.product.domain.service.productstockhistory;

import com.coopang.product.domain.entity.productstock.ProductStockEntity;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productstockhistory.ProductStockHistoryEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductStockHistoryDomainService {

    public ProductStockHistoryEntity create(ProductStockEntity productStockEntity,int stock){

        //3. 재고 기록엔티티 생성
        ProductStockHistoryEntity productStockHistoryEntity = makeProductStockHistory(productStockEntity,stock);

        return productStockHistoryEntity;
    }

    private ProductStockHistoryEntity makeProductStockHistory(
        ProductStockEntity productStockEntity,int stock
    ) {
        return ProductStockHistoryEntity.create(
            null,
            productStockEntity,
            null,
            ProductStockHistoryChangeType.INCREASE,
            stock,
            0,
            stock,
            null
        );
    }
}
