package com.coopang.product.domain.service.product;


import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.domain.entity.category.CategoryEntity;
import com.coopang.product.domain.entity.product.ProductEntity;
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

        ProductEntity productEntity = productDtoToProductEntity(productDto);
        ProductEntity newProductEntity = productJpaRepository.save(productEntity);

        return newProductEntity;
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
}
