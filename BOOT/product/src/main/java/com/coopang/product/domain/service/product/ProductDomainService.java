package com.coopang.product.domain.service.product;


import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
import com.coopang.product.domain.entity.productStock.ProductStockEntity;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.productStockHistory.ProductStockHistoryEntity;
import com.coopang.product.domain.entity.productStock.ProductStock;
import com.coopang.product.domain.repository.category.CategoryRepository;
import com.coopang.product.domain.repository.product.ProductRepository;
import com.coopang.product.infrastructure.repository.category.CategoryJpaRepository;
import com.coopang.product.infrastructure.repository.product.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductDomainService {

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public ProductEntity create(ProductDto productDto) {

        //1. 상품 생성
        ProductEntity productEntity = productDtoToProductEntity(productDto);

        //2. 재고엔티티 생성
        ProductStock stock = new ProductStock(productDto.getProductStock());
        ProductStockEntity productStockEntity = productDtoToProductStockEntity(productEntity,stock);

        //3. 재고 기록엔티티 생성
        ProductStockHistoryEntity productStockHistoryEntity = makeProductStockHistory(productStockEntity,stock.getValue());

        //4. 연관관계 설정
        productEntity.addProductStockEntity(productStockEntity);
        productStockEntity.addStockHistory(productStockHistoryEntity);

        return productJpaRepository.save(productEntity);
    }


    private ProductEntity productDtoToProductEntity(ProductDto productDto) {

        CategoryEntity categoryEntity = categoryJpaRepository.findById(productDto.getCategoryId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        return ProductEntity.create(
            null,
            categoryEntity,
            productDto.getCompanyId(),
            productDto.getProductName(),
            productDto.getProductPrice(),
            false,
            true
        );
    }

    private ProductStockEntity productDtoToProductStockEntity(ProductEntity productEntity,ProductStock  stock) {

        return ProductStockEntity.create(
            null,
            productEntity,
            stock
        );
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
