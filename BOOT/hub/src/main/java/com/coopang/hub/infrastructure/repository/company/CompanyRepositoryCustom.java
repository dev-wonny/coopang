package com.coopang.hub.infrastructure.repository.company;

import com.coopang.hub.application.request.company.CompanySearchCondition;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyRepositoryCustom {
    Page<CompanyEntity> search(CompanySearchCondition condition, Pageable pageable);

    List<CompanyEntity> findCompanyList(CompanySearchCondition condition);

}
