package com.coopang.product.application.response.product;

import com.coopang.product.domain.entity.product.ProductEntity;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ProductResponseDto(
    UUID productId,
    UUID categoryId,
    String categoryName,
    UUID companyId,
    String productName,
    double productPrice,
    int productStock
) {

    public static ProductResponseDto of(ProductEntity product) {
        return ProductResponseDto.builder()
            .productId(product.getProductId())
            .categoryId(product.getCategoryEntity().getCategoryId())
            .categoryName(product.getCategoryEntity().getCategoryName())
            .companyId(product.getCompanyId())
            .productName(product.getProductName())
            .productPrice(product.getProductPrice())
            .productStock(product.getProductStockEntity().getProductStock().getValue())
            .build();
    }
}

