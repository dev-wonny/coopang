package com.coopang.hub.domain.service.company;

import com.coopang.hub.application.request.company.CompanyDto;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import com.coopang.hub.infrastructure.repository.company.CompanyJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyDomainService {
    private final CompanyJpaRepository companyJpaRepository;

    public CompanyDomainService(CompanyJpaRepository companyJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
    }

    public CompanyEntity createCompany(CompanyDto companyDto) {
        CompanyEntity companyEntity = CompanyEntity.create(
            companyDto.getCompanyId()
            , companyDto.getHubId()
            , companyDto.getCompanyManagerId()
            , companyDto.getCompanyName()
            , companyDto.getZipCode()
            , companyDto.getAddress1()
            , companyDto.getAddress2()
        );
        return companyJpaRepository.save(companyEntity);
    }
}
