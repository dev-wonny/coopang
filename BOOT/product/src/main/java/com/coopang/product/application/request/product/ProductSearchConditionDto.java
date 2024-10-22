package com.coopang.product.application.request.product;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

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

    public static ProductSearchConditionDto from(
         UUID productId,
         String productName,
         UUID companyId,
         double minProductPrice,
         double maxProductPrice,
         LocalDateTime startDate,
         LocalDateTime endDate,
         boolean isAbleToWatchDeleted
    ) {
        return ProductSearchConditionDto.builder()
            .productId(productId)
            .productName(productName)
            .companyId(companyId)
            .minProductPrice(minProductPrice)
            .maxProductPrice(maxProductPrice)
            .startDate(startDate)
            .endDate(endDate)
            .isAbleToWatchDeleted(isAbleToWatchDeleted)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductSearchConditionDto that = (ProductSearchConditionDto) o;
        return Double.compare(minProductPrice, that.minProductPrice) == 0
            && Double.compare(maxProductPrice, that.maxProductPrice) == 0
            && isAbleToWatchDeleted == that.isAbleToWatchDeleted && Objects.equals(productId,
            that.productId) && Objects.equals(productName, that.productName)
            && Objects.equals(companyId, that.companyId) && Objects.equals(
            startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, companyId, minProductPrice, maxProductPrice,
            startDate, endDate, isAbleToWatchDeleted);
    }

    @Override
    public String toString() {
        return "ProductSearchConditionDto{" +
            "productId=" + productId +
            ", productName='" + productName + '\'' +
            ", companyId=" + companyId +
            ", minProductPrice=" + minProductPrice +
            ", maxProductPrice=" + maxProductPrice +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", isAbleToWatchDeleted=" + isAbleToWatchDeleted +
            '}';
    }
}
