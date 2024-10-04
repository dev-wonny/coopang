package com.coopang.product.domain.service;


import com.coopang.product.application.request.ProductDto;
import com.coopang.product.domain.entity.CategoryEntity;
import com.coopang.product.domain.entity.ProductEntity;
import com.coopang.product.domain.entity.ProductStockEntity;
import com.coopang.product.domain.entity.ProductStockHistoryChangeType;
import com.coopang.product.domain.entity.ProductStockHistoryEntity;
import com.coopang.product.domain.entity.vo.ProductStock;
import com.coopang.product.domain.repository.CategoryRepository;
import com.coopang.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductDomainService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

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

        return productRepository.save(productEntity);
    }


    private ProductEntity productDtoToProductEntity(ProductDto productDto) {

        CategoryEntity categoryEntity = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        return ProductEntity.create(
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
            productEntity,
            stock
        );
    }

    private ProductStockHistoryEntity makeProductStockHistory(
        ProductStockEntity productStockEntity,int stock
    ) {
        return ProductStockHistoryEntity.create(
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
