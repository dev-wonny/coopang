package com.coopang.product.application.request.product;

import com.coopang.product.presentation.request.product.ProductSearchConditionRequestDto;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductSearchConditionDto {

    private UUID productId;
    private String productName;
    private UUID companyId;
    private double minProductPrice;
    private double maxProductPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isAbleToWatchDeleted;

    public static ProductSearchConditionDto from(ProductSearchConditionRequestDto requestDto) {
        return ProductSearchConditionDto.builder()
            .productId(requestDto.getProductId())
            .productName(requestDto.getProductName())
            .companyId(requestDto.getCompanyId())
            .minProductPrice(requestDto.getMinProductPrice())
            .maxProductPrice(requestDto.getMaxProductPrice())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .isAbleToWatchDeleted(requestDto.getIsAbleToWatchDeleted())
            .build();
    }

    public static ProductSearchConditionDto empty(){
        return ProductSearchConditionDto.builder()
            .productId(null)
            .productName(null)
            .companyId(null)
            .minProductPrice(0.0)
            .maxProductPrice(0.0)
            .startDate(null)
            .endDate(null)
            .isAbleToWatchDeleted(false)
            .build();
    }

    public void setIsAbleToWatchDeleted() {
        this.isAbleToWatchDeleted = true;
    }

    public boolean getIsAbleToWatchDeleted(){
        return this.isAbleToWatchDeleted;
    }
}
