package com.coopang.delivery.infrastructure.repository.deliveryhubhistory;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;
import com.querydsl.core.types.Projections;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

import static com.coopang.delivery.domain.entity.deliveryhubhistory.QDeliveryHubHistoryEntity.deliveryHubHistoryEntity;

@Repository
public class DeliveryHubHistoryRepositoryCustomImpl extends Querydsl4RepositorySupport implements DeliveryHubHistoryRepositoryCustom {

    public DeliveryHubHistoryRepositoryCustomImpl() {
        super(DeliveryHubHistoryEntity.class);
    }

    @Override
    public List<DeliveryHubHistoryEntity> findByDeliveryId(UUID deliveryId) {
        return select(Projections.fields(DeliveryHubHistoryEntity.class, // 필요한 필드만 선택
                deliveryHubHistoryEntity.deliveryId,
                deliveryHubHistoryEntity.departureHubId,
                deliveryHubHistoryEntity.arrivalHubId,
                deliveryHubHistoryEntity.deliveryRouteHistoryStatus,
                deliveryHubHistoryEntity.createdAt,
                deliveryHubHistoryEntity.hubShipperId
        ))
                .from(deliveryHubHistoryEntity)
                .where(deliveryIdEq(deliveryId))
                .fetch(); // 결과를 리스트로 가져오기
    }

    private Predicate deliveryIdEq(UUID deliveryId) {
        return StringUtils.hasText(String.valueOf(deliveryId)) ? deliveryHubHistoryEntity.deliveryId.eq(deliveryId) : null;
    }
}
