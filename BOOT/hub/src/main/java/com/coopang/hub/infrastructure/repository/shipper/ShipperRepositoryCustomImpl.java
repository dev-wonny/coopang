package com.coopang.hub.infrastructure.repository.shipper;

import static com.coopang.hub.domain.entity.hub.QHubEntity.hubEntity;
import static com.coopang.hub.domain.entity.shipper.QShipperEntity.shipperEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.hub.application.request.shipper.ShipperSearchCondition;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class ShipperRepositoryCustomImpl extends Querydsl4RepositorySupport implements ShipperRepositoryCustom {
    public ShipperRepositoryCustomImpl() {
        super(ShipperEntity.class);
    }

    @Override
    public Page<ShipperEntity> search(ShipperSearchCondition condition, Pageable pageable) {

        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(shipperEntity)
                        .innerJoin(hubEntity).on(shipperEntity.hubId.eq(hubEntity.hubId))
                        .where(
                                hubIdEq(condition.getHubId())
                                , shipperTypeEq(condition.getShipperType())
                                , hubNameStartsWith(condition.getHubName())
                                , shipperEntity.isDeleted.eq(condition.getIsDeleted())
                        ),
                countQuery -> countQuery
                        .selectFrom(shipperEntity)
                        .innerJoin(hubEntity).on(shipperEntity.hubId.eq(hubEntity.hubId))
                        .where(
                                hubIdEq(condition.getHubId())
                                , shipperTypeEq(condition.getShipperType())
                                , hubNameStartsWith(condition.getHubName())
                                , shipperEntity.isDeleted.eq(condition.getIsDeleted())
                        )
        );
    }

    @Override
    public List<ShipperEntity> findShipperList(ShipperSearchCondition condition) {
        return selectFrom(shipperEntity)
                .innerJoin(hubEntity).on(shipperEntity.hubId.eq(hubEntity.hubId))
                .where(
                        hubIdEq(condition.getHubId())
                        , shipperTypeEq(condition.getShipperType())
                        , hubNameStartsWith(condition.getHubName())
                        , shipperEntity.isDeleted.eq(condition.getIsDeleted())
                )
                .fetch();
    }

    private Predicate hubIdEq(UUID hubId) {
        return hubId != null ? shipperEntity.hubId.eq(hubId) : null;
    }

    private Predicate shipperTypeEq(ShipperTypeEnum shipperType) {
        return shipperType != null ? shipperEntity.shipperType.eq(shipperType) : null;
    }

    private Predicate hubNameStartsWith(String hubName) {
        return StringUtils.hasText(hubName) ? hubEntity.hubName.startsWith(hubName) : null;
    }
}
