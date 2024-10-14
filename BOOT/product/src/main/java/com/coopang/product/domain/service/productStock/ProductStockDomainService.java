package com.coopang.product.domain.service.productStock;

import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStock;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductStockDomainService {

    public ProductStockEntity create(int quantity, ProductEntity productEntity){
        //2. 재고엔티티 생성
        ProductStock stock = new ProductStock(quantity);
        ProductStockEntity productStockEntity = productDtoToProductStockEntity(productEntity,stock);

        return productStockEntity;
    }

    private ProductStockEntity productDtoToProductStockEntity(ProductEntity productEntity,ProductStock  stock) {

        return ProductStockEntity.create(
            null,
            productEntity,
            stock
        );
    }
}
