package com.coopang.product.application.request.product;

import com.coopang.product.presentation.request.product.ProductBaseSearchConditionRequestDto;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductBaseSearchConditionDto {
    private UUID hubId;
    private UUID companyId;

    public static ProductBaseSearchConditionDto from(ProductBaseSearchConditionRequestDto requestDto){
        return ProductBaseSearchConditionDto.builder()
            .hubId(requestDto.getHubId())
            .companyId(requestDto.getCompanyId())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductBaseSearchConditionDto that = (ProductBaseSearchConditionDto) o;
        return Objects.equals(hubId, that.hubId) && Objects.equals(companyId,
            that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hubId, companyId);
    }

    @Override
    public String toString() {
        return "ProductBaseSearchConditionDto{" +
            "hubId=" + hubId +
            ", companyId=" + companyId +
            '}';
    }
}
