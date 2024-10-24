package com.coopang.delivery.infrastructure.repository.deliveryuserhistory;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.delivery.domain.entity.deliveryhubhistory.DeliveryHubHistoryEntity;
import com.coopang.delivery.domain.entity.deliveryuserhistory.DeliveryUserHistoryEntity;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

import static com.coopang.delivery.domain.entity.deliveryuserhistory.QDeliveryUserHistoryEntity.deliveryUserHistoryEntity;

@Repository
public class DeliveryUserHistoryRepositoryCustomImpl extends Querydsl4RepositorySupport implements DeliveryUserHistoryRepositoryCustom {

    public DeliveryUserHistoryRepositoryCustomImpl() {
        super(DeliveryHubHistoryEntity.class);
    }

    @Override
    public List<DeliveryUserHistoryEntity> findByDeliveryId(UUID deliveryId) {
        return select(Projections.fields(DeliveryUserHistoryEntity.class, // 필요한 필드만 선택
                deliveryUserHistoryEntity.deliveryId
                , deliveryUserHistoryEntity.departureHubId
                , deliveryUserHistoryEntity.deliveryRouteHistoryStatus
                , deliveryUserHistoryEntity.createdAt
                , deliveryUserHistoryEntity.userShipperId
        ))
                .from(deliveryUserHistoryEntity)
                .where(deliveryIdEq(deliveryId))
                .fetch(); // 결과를 리스트로 가져오기
    }

    private Predicate deliveryIdEq(UUID deliveryId) {
        return StringUtils.hasText(String.valueOf(deliveryId)) ? deliveryUserHistoryEntity.deliveryId.eq(deliveryId) : null;
    }
}