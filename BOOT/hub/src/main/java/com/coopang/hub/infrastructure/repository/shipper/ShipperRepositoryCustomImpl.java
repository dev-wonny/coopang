package com.coopang.hub.infrastructure.repository.shipper;

import static com.coopang.hub.domain.entity.hub.QHubEntity.hubEntity;
import static com.coopang.hub.domain.entity.shipper.QShipperEntity.shipperEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.hub.application.request.shipper.ShipperSearchConditionDto;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class ShipperRepositoryCustomImpl extends Querydsl4RepositorySupport implements ShipperRepositoryCustom {
    public ShipperRepositoryCustomImpl() {
        super(ShipperEntity.class);
    }

    @Override
    public Page<ShipperEntity> search(ShipperSearchConditionDto condition, Pageable pageable) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(shipperEntity)
                .innerJoin(hubEntity).on(shipperEntity.hubId.eq(hubEntity.hubId))
                .where(
                    whereClause
                ),
            countQuery -> countQuery
                .selectFrom(shipperEntity)
                .innerJoin(hubEntity).on(shipperEntity.hubId.eq(hubEntity.hubId))
                .where(
                    whereClause
                )
        );
    }

    @Override
    public List<ShipperEntity> findShipperList(ShipperSearchConditionDto condition) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return selectFrom(shipperEntity)
            .innerJoin(hubEntity).on(shipperEntity.hubId.eq(hubEntity.hubId))
            .where(
                whereClause
            )
            .fetch();
    }

    private BooleanBuilder generateWhereClause(ShipperSearchConditionDto condition) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(shipperIdEq(condition.getShipperId()));
        whereClause.and(hubIdEq(condition.getHubId()));
        whereClause.and(shipperTypeEq(condition.getShipperType()));
        whereClause.and(hubNameStartsWith(condition.getHubName()));
        whereClause.and(shipperEntity.isDeleted.eq(condition.isDeleted()));
        whereClause.and(hubEntity.isDeleted.eq(condition.isDeleted()));
        return whereClause;
    }

    private Predicate shipperIdEq(UUID shipperId) {
        return !ObjectUtils.isEmpty(shipperId) ? shipperEntity.shipperId.eq(shipperId) : null;
    }

    private Predicate hubIdEq(UUID hubId) {
        return !ObjectUtils.isEmpty(hubId) ? shipperEntity.hubId.eq(hubId) : null;
    }

    private Predicate shipperTypeEq(ShipperTypeEnum shipperType) {
        return !ObjectUtils.isEmpty(shipperType) ? shipperEntity.shipperType.eq(shipperType) : null;
    }

    private Predicate hubNameStartsWith(String hubName) {
        return StringUtils.hasText(hubName) ? hubEntity.hubName.startsWith(hubName) : null;
    }
}
