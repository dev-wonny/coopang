package com.coopang.hub.infrastructure.repository.company;

import static com.coopang.hub.domain.entity.company.QCompanyEntity.companyEntity;
import static com.coopang.hub.domain.entity.hub.QHubEntity.hubEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.hub.application.request.company.CompanySearchConditionDto;
import com.coopang.hub.domain.entity.company.CompanyEntity;
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
public class CompanyRepositoryCustomImpl extends Querydsl4RepositorySupport implements CompanyRepositoryCustom {
    public CompanyRepositoryCustomImpl() {
        super(CompanyEntity.class);
    }

    @Override
    public Page<CompanyEntity> search(CompanySearchConditionDto condition, Pageable pageable) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(companyEntity)
                .innerJoin(hubEntity).on(companyEntity.hubId.eq(hubEntity.hubId))
                .where(
                    whereClause
                ),
            countQuery -> countQuery
                .selectFrom(companyEntity)
                .innerJoin(hubEntity).on(companyEntity.hubId.eq(hubEntity.hubId))
                .where(
                    whereClause
                )
        );

    }

    @Override
    public List<CompanyEntity> findCompanyList(CompanySearchConditionDto condition) {
        final BooleanBuilder whereClause = generateWhereClause(condition);
        return selectFrom(companyEntity)
            .innerJoin(hubEntity).on(companyEntity.hubId.eq(hubEntity.hubId))
            .where(
                whereClause
            )
            .fetch();
    }

    private BooleanBuilder generateWhereClause(CompanySearchConditionDto condition) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(companyIdEq(condition.getCompanyId()));
        whereClause.and(hubIdEq(condition.getHubId()));
        whereClause.and(companyManagerIdEq(condition.getCompanyManagerId()));
        whereClause.and(companyNameStartsWith(condition.getCompanyName()));
        whereClause.and(hubNameStartsWith(condition.getHubName()));
        whereClause.and(companyEntity.isDeleted.eq(condition.isDeleted()));
        whereClause.and(hubEntity.isDeleted.eq(condition.isDeleted()));
        return whereClause;
    }

    private Predicate companyIdEq(UUID companyId) {
        return !ObjectUtils.isEmpty(companyId) ? companyEntity.companyId.eq(companyId) : null;
    }

    private Predicate hubIdEq(UUID hubId) {
        return !ObjectUtils.isEmpty(hubId) ? companyEntity.hubId.eq(hubId) : null;
    }

    private Predicate companyManagerIdEq(UUID companyManagerId) {
        return !ObjectUtils.isEmpty(companyManagerId) ? companyEntity.companyManagerId.eq(companyManagerId) : null;
    }

    private Predicate companyNameStartsWith(String companyName) {
        return StringUtils.hasText(companyName) ? companyEntity.companyName.startsWith(companyName) : null;
    }

    private Predicate hubNameStartsWith(String hubName) {
        return StringUtils.hasText(hubName) ? hubEntity.hubName.startsWith(hubName) : null;
    }
}
