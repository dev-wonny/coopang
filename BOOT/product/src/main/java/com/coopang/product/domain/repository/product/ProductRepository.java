package com.coopang.product.domain.repository.product;

import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.presentation.request.product.ProductSearchConditionDto;
import com.coopang.product.presentation.request.productStockHistory.ProductStockHistorySearchConditionDto;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository {

    Optional<ProductEntity> findByProductId(UUID productId);

    //카테고리와 같이 조회
    Optional<ProductEntity> getOneByProductIdWithCategory(UUID productId);

    Optional<ProductEntity> findByProductIdAndIsDeletedFalse(UUID productId);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c WHERE p.isDeleted = false and p.isHidden = false and p.isSale = true and s.productStock.value > 0")
    Page<ProductEntity> findAllWithStockAndCategory(Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c WHERE p.companyId = :companyId")
    Page<ProductEntity> findAllWithStockAndCategoryByCompanyId(UUID companyId,Pageable pageable);

    Page<ProductEntity> search(ProductSearchConditionDto productSearchCondition, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c "
        + "WHERE p.categoryEntity.categoryId = :categoryId and p.isDeleted = false and p.isHidden = false and p.isSale = true and s.productStock.value > 0" )
    Page<ProductEntity> findAllWithStockAndCategoryByCategoryId(UUID categoryId,Pageable pageable);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productStockEntity s JOIN FETCH p.categoryEntity c")
    Page<ProductEntity> findAll(Pageable pageable);

    Page<ProductStockHistoryEntity> getProductStockHistoryByProductId(UUID productId, Pageable pageable);

    Page<ProductStockHistoryEntity> searchProductStockHistoryByProductId(
        ProductStockHistorySearchConditionDto condition,UUID productId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_READ) // 또는 PESSIMISTIC_WRITE
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :productId")
    Optional<ProductEntity> findByProductIdWithLock(UUID productId);

    ProductStockEntity findAndLockProductStock(UUID productId);
}
