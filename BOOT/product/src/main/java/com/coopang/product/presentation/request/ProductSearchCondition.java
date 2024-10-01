package com.coopang.product.presentation.request;

import java.util.UUID;

public record ProductSearchCondition(
    UUID hubId,
    UUID companyId
) {

}
