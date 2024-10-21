package com.coopang.hub.infrastructure.repository.company;

import com.coopang.hub.application.request.company.CompanySearchConditionDto;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyRepositoryCustom {
    Page<CompanyEntity> search(CompanySearchConditionDto condition, Pageable pageable);

    List<CompanyEntity> findCompanyList(CompanySearchConditionDto condition);

}
