package com.coopang.product.application.service;

import com.coopang.product.application.response.ProductWithStockResponseDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.response.productStock.ProductStockResponseDto;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.application.service.productstock.ProductStockService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductWithStockService {

    private final ProductService productService;
    private final ProductStockService productStockService;

    //특정 상품의 정보와 재고를 함께 조회 - 삭제된 상품 미포함
    public ProductWithStockResponseDto getValidProductWithStockById(UUID productId)
    {
        //캐시가 적용된 상품정보 조회
        ProductResponseDto productResponseDto = productService.getValidProductById(productId);
        //캐시가 적용되지 않은 재고 조회
        ProductStockResponseDto productStockResponseDto = productStockService.getProductStockByProductId(productId);

        return ProductWithStockResponseDto.of(productResponseDto, productStockResponseDto);
    }

    //특정 상품의 정보와 재고를 함께 조회 - 삭제된 상품 포함
    public ProductWithStockResponseDto getProductWithStockById(UUID productId)
    {
        //캐시가 적용된 상품정보 조회
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        //캐시가 적용되지 않은 재고 조회
        ProductStockResponseDto productStockResponseDto = productStockService.getProductStockByProductId(productId);

        return ProductWithStockResponseDto.of(productResponseDto, productStockResponseDto);
    }
}
