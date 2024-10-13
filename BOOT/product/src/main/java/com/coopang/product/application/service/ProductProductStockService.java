package com.coopang.product.application.service;

import com.coopang.product.application.request.product.ProductDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.application.service.productstock.ProductStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductProductStockService {
    private final ProductService productService;
    private final ProductStockService productStockService;

    public ProductProductStockService(ProductService productService, ProductStockService productStockService) {
        this.productService = productService;
        this.productStockService = productStockService;
    }


    public ProductResponseDto createProductAndProductStock(ProductDto productDto) {
        ProductResponseDto newProductDto = productService.createProduct(productDto);
        Integer productStock = productStockService.createProductStock(productDto.getProductStock());

        return newProductDto;
    }
}
