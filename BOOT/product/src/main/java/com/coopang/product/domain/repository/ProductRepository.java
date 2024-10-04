package com.coopang.product.domain.repository;

import com.coopang.product.domain.entity.ProductEntity;
import com.coopang.product.presentation.request.ProductSearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository {

    Optional<ProductEntity> getOneByProductId(UUID productId);

    ProductEntity save(ProductEntity productEntity);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c WHERE p.isDeleted = false and p.isHidden = false and p.isSale = true and s.productStock.value > 0")
    Page<ProductEntity> findAllWithStockAndCategory(Pageable pageable);

    Page<ProductEntity> search(ProductSearchCondition productSearchCondition, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c "
        + "WHERE p.categoryEntity.categoryId = :categoryId and p.isDeleted = false and p.isHidden = false and p.isSale = true and s.productStock.value > 0" )
    Page<ProductEntity> findAllWithStockAndCategoryByCategoryId(UUID categoryId,Pageable pageable);

}
