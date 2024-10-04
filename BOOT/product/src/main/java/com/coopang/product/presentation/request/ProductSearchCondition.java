package com.coopang.product.presentation.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductSearchCondition(
    UUID hubId,
    UUID companyId,
    String productName,
    double minProductPrice,
    double maxProductPrice,
    LocalDateTime startDate,
    LocalDateTime endDate
) {

}
