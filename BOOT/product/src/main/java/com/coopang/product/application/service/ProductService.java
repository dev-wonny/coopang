package com.coopang.product.application.service;

import com.coopang.product.application.request.ProductDto;
import com.coopang.product.application.response.productResponseDto;
import com.coopang.product.domain.entitiy.ProductEntity;
import com.coopang.product.domain.repository.ProductRepository;
import com.coopang.product.domain.service.ProductDomainService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;

    public productResponseDto createProduct(ProductDto productDto) {

        ProductEntity productEntity = productDomainService.create(productDto);

        return productResponseDto.of(productEntity);
    }
}
