package com.coopang.hub.infrastructure.repository.company;

import static com.coopang.hub.domain.entity.company.QCompanyEntity.companyEntity;
import static com.coopang.hub.domain.entity.hub.QHubEntity.hubEntity;

import com.coopang.apiconfig.querydsl.Querydsl4RepositorySupport;
import com.coopang.hub.application.request.company.CompanySearchCondition;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class CompanyRepositoryCustomImpl extends Querydsl4RepositorySupport implements CompanyRepositoryCustom {
    public CompanyRepositoryCustomImpl() {
        super(CompanyEntity.class);
    }

    @Override
    public Page<CompanyEntity> search(CompanySearchCondition condition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(companyEntity)
                        .innerJoin(hubEntity).on(companyEntity.hubId.eq(hubEntity.hubId))
                        .where(
                                companyNameStartsWith(condition.getCompanyName())
                                , companyManagerIdEq(condition.getCompanyManagerId())
                                , hubNameStartsWith(condition.getHubName())
                                , companyEntity.isDeleted.eq(condition.getIsDeleted())
                                , hubEntity.isDeleted.eq(condition.getIsDeleted())
                        ),
                countQuery -> countQuery
                        .selectFrom(companyEntity)
                        .innerJoin(hubEntity).on(companyEntity.hubId.eq(hubEntity.hubId))
                        .where(
                                companyNameStartsWith(condition.getCompanyName())
                                , companyManagerIdEq(condition.getCompanyManagerId())
                                , hubNameStartsWith(condition.getHubName())
                                , companyEntity.isDeleted.eq(condition.getIsDeleted())
                                , hubEntity.isDeleted.eq(condition.getIsDeleted())
                        )
        );

    }

    @Override
    public List<CompanyEntity> findCompanyList(CompanySearchCondition condition) {
        return selectFrom(companyEntity)
                .innerJoin(hubEntity).on(companyEntity.hubId.eq(hubEntity.hubId))
                .where(
                        companyNameStartsWith(condition.getCompanyName())
                        , companyManagerIdEq(condition.getCompanyManagerId())
                        , hubNameStartsWith(condition.getHubName())
                        , companyEntity.isDeleted.eq(condition.getIsDeleted())
                        , hubEntity.isDeleted.eq(condition.getIsDeleted())
                )
                .fetch();
    }

    private Predicate companyNameStartsWith(String companyName) {
        return StringUtils.hasText(companyName) ? companyEntity.companyName.startsWith(companyName) : null;
    }

    private Predicate companyManagerIdEq(UUID companyManagerId) {
        return companyManagerId != null ? companyEntity.companyManagerId.eq(companyManagerId) : null;
    }

    private Predicate hubNameStartsWith(String hubName) {
        return StringUtils.hasText(hubName) ? hubEntity.hubName.startsWith(hubName) : null;
    }
}
