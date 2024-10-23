package com.coopang.delivery.infrastructure.repository.delivery;

import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.presentation.request.delivery.DeliverySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepositoryCustom {
    Page<DeliveryEntity> search(DeliverySearchCondition condition, Pageable pageable);
}
