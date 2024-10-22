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
        } else if (o != null && this.getClass() == o.getClass()) {
            ProductSearchConditionDto that = (ProductSearchConditionDto) o;
            return
                Objects.equals(this.productId, that.productId)
                    && Objects.equals(this.productName, that.productName)
                    && Objects.equals(this.companyId ,that.companyId)
                    && Objects.equals(this.minProductPrice, that.minProductPrice)
                    && Objects.equals(this.maxProductPrice, that.maxProductPrice)
                    && Objects.equals(this.startDate, that.startDate)
                    && Objects.equals(this.endDate, that.endDate)
                    && this.isAbleToWatchDeleted == that.isAbleToWatchDeleted;

        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.productId, this.productName, this.companyId, this.minProductPrice, this.maxProductPrice,
            this.startDate, this.endDate, this.isAbleToWatchDeleted);
    }

    @Override
    public String toString() {

        return  "ProductSearchConditionDto(productId=" + this.productId
            + ", productName=" + this.productName
            + ", companyId=" + this.companyId
            + ", minProductPrice=" + this.minProductPrice
            + ", maxProductPrice=" + this.maxProductPrice
            + ", startDate=" + this.startDate
            + ", endDate=" + this.endDate
            + ", isAbleToWatchDeleted=" + this.isAbleToWatchDeleted
            + ")";
    }
}
