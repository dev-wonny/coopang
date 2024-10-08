package com.coopang.product.presentation.request.product;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseSearchCondition {
    private UUID hubId;
    private UUID companyId;
}
