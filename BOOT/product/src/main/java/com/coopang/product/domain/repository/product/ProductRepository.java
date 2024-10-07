package com.coopang.product.domain.repository.product;

import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.product.ProductSearchCondition;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchCondition;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository {

    Optional<ProductEntity> getOneByProductId(UUID productId);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c WHERE p.isDeleted = false and p.isHidden = false and p.isSale = true and s.productStock.value > 0")
    Page<ProductEntity> findAllWithStockAndCategory(Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c WHERE p.companyId = :companyId")
    Page<ProductEntity> findAllWithStockAndCategoryByCompanyId(UUID companyId,Pageable pageable);

    Page<ProductEntity> search(ProductSearchCondition productSearchCondition, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c "
        + "WHERE p.categoryEntity.categoryId = :categoryId and p.isDeleted = false and p.isHidden = false and p.isSale = true and s.productStock.value > 0" )
    Page<ProductEntity> findAllWithStockAndCategoryByCategoryId(UUID categoryId,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c")
    Page<ProductEntity> findAll(Pageable pageable);

    Page<ProductStockHistoryEntity> getProductStockHistoryByProductId(UUID productId, Pageable pageable);

    Page<ProductStockHistoryEntity> searchProductStockHistoryByProductId(
        ProductStockHistorySearchCondition condition,UUID productId, Pageable pageable);

}
