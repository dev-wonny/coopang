package com.coopang.product.application.response;

import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.response.productStock.ProductStockResponseDto;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductWithStockResponseDto {
    private UUID productId;
    private UUID categoryId;
    private String categoryName;
    private UUID companyId;
    private String productName;
    private double productPrice;
    private int stock;

    public static ProductWithStockResponseDto of(ProductResponseDto productResponseDto, ProductStockResponseDto productStockResponseDto) {
        return ProductWithStockResponseDto.builder()
            .productId(productResponseDto.getProductId())
            .categoryId(productResponseDto.getCategoryId())
            .categoryName(productResponseDto.getCategoryName())
            .companyId(productResponseDto.getCompanyId())
            .productName(productResponseDto.getProductName())
            .productPrice(productResponseDto.getProductPrice())
            .stock(productStockResponseDto.getStock())
                .build();

    }
}
