package com.coopang.product.infrastructure.repository;

import com.coopang.product.domain.entity.ProductEntity;
import com.coopang.product.domain.entity.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.ProductSearchCondition;
import com.coopang.product.presentation.request.ProductStockHistorySearchCondition;
import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Optional<ProductEntity> getOneByProductId(UUID productId);

    Page<ProductEntity> search(ProductSearchCondition productSearchCondition, Pageable pageable);

    Page<ProductStockHistoryEntity> getProductStockHistoryByProductId(UUID productId, Pageable pageable);

    Page<ProductStockHistoryEntity> searchProductStockHistoryByProductId(
        ProductStockHistorySearchCondition condition,UUID productId, Pageable pageable);

}
