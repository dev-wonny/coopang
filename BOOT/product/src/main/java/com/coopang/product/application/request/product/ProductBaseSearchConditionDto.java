package com.coopang.product.application.request.product;

import com.coopang.product.presentation.request.product.ProductBaseSearchConditionRequestDto;
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
}
