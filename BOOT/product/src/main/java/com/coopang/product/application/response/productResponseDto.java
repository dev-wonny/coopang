package com.coopang.product.application.response;

import com.coopang.product.domain.entitiy.ProductEntity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record productResponseDto(
    UUID productId,
    String categoryName,
    UUID companyId,
    String ProductName,
    double productPrice,
    int productStock
) {

    public static productResponseDto of(ProductEntity product) {
        return productResponseDto.builder()
            .productId(product.getProductId())
            .categoryName(product.getCategoryEntity().getCategoryName())
            .companyId(product.getCompanyId())
            .ProductName(product.getProductName())
            .productPrice(product.getProductPrice())
            .productStock(product.getProductStockEntity().getProductStock().getValue())
            .build();
    }
}
