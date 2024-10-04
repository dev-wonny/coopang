package com.coopang.product.domain.repository;

import com.coopang.product.domain.entitiy.ProductEntity;
import com.coopang.product.presentation.request.ProductSearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository {

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c WHERE p.productId = :productId")
    Optional<ProductEntity> findById(UUID productId);

    ProductEntity save(ProductEntity productEntity);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c")
    Page<ProductEntity> findAllWithStockAndCategory(Pageable pageable);

    Page<ProductEntity> search(ProductSearchCondition productSearchCondition, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c WHERE p.categoryEntity.categoryId = :categoryId" )
    Page<ProductEntity> findAllWithStockAndCategoryByCategoryId(UUID categoryId,Pageable pageable);

}
