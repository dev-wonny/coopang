package com.coopang.product.application.service;

import com.coopang.product.application.request.ProductDto;
import com.coopang.product.application.response.ProductResponseDto;
import com.coopang.product.domain.entitiy.ProductEntity;
import com.coopang.product.domain.repository.CategoryRepository;
import com.coopang.product.domain.repository.ProductRepository;
import com.coopang.product.domain.service.ProductDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    private final CategoryRepository categoryRepository;

    public ProductResponseDto createProduct(ProductDto productDto) {

        ProductEntity productEntity = productDomainService.create(productDto);

        return ProductResponseDto.of(productEntity);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID productId) {

        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return ProductResponseDto.of(productEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {

        Page<ProductEntity> productEntities = productRepository.findAll(pageable);

        return productEntities.map(ProductResponseDto::of);
    }

    public Page<ProductResponseDto> getProductWithCategory(UUID categoryId, Pageable pageable) {

        categoryRepository.findById(categoryId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        return null;
    }
}
