package com.coopang.hub.domain.repository.company;

import com.coopang.hub.application.request.company.CompanySearchCondition;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Optional<CompanyEntity> findByCompanyId(UUID companyId);

    Optional<CompanyEntity> findByCompanyIdAndIsDeletedFalse(UUID companyId);

    Page<CompanyEntity> findAllByIsDeletedFalse(Pageable pageable);

    List<CompanyEntity> findCompanyList(CompanySearchCondition condition);

    Page<CompanyEntity> search(CompanySearchCondition condition, Pageable pageable);
}
