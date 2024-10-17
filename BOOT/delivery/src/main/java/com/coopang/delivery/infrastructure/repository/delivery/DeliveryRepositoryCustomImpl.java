package com.coopang.delivery.infrastructure.repository.delivery;

import static com.coopang.delivery.domain.entity.delivery.QDeliveryEntity.deliveryEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.delivery.domain.entity.delivery.DeliveryEntity;
import com.coopang.delivery.presentation.request.delivery.DeliverySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.querydsl.core.types.Predicate;

import java.util.UUID;

@Repository
public class DeliveryRepositoryCustomImpl extends Querydsl4RepositorySupport implements DeliveryRepositoryCustom {

    public DeliveryRepositoryCustomImpl() {
        super(DeliveryEntity.class);
    }

    @Override
    public Page<DeliveryEntity> search(DeliverySearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(deliveryEntity)
                        .where(destinationHubIdEq(condition.getDestinationHubId())),
                countQuery -> countQuery
                        .selectFrom(deliveryEntity)
                        .where(destinationHubIdEq(condition.getDestinationHubId()))

        );

    }

    private Predicate destinationHubIdEq(UUID destinationHubId) {
        return StringUtils.hasText(String.valueOf(destinationHubId)) ? deliveryEntity.destinationHubId.eq(destinationHubId) : null;
    }
}
