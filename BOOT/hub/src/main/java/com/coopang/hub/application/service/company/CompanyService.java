package com.coopang.hub.application.service.company;

import com.coopang.apiconfig.error.AccessDeniedException;
import com.coopang.hub.application.request.company.CompanyDto;
import com.coopang.hub.application.request.company.CompanySearchCondition;
import com.coopang.hub.application.response.company.CompanyResponseDto;
import com.coopang.hub.domain.entity.company.CompanyEntity;
import com.coopang.hub.domain.repository.company.CompanyRepository;
import com.coopang.hub.domain.service.company.CompanyDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "CompanyService")
@Transactional
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyDomainService companyDomainService;

    public CompanyService(CompanyRepository companyRepository, CompanyDomainService companyDomainService) {
        this.companyRepository = companyRepository;
        this.companyDomainService = companyDomainService;
    }

    @Transactional
    public CompanyResponseDto createCompany(CompanyDto companyDto) {
        // 서비스 레이어에서 UUID 생성
        final UUID userId = companyDto.getCompanyId() != null ? companyDto.getCompanyId() : UUID.randomUUID();
        companyDto.setCompanyId(userId);

        CompanyEntity companyEntity = companyDomainService.createCompany(companyDto);
        return CompanyResponseDto.fromCompany(companyEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "companies", key = "#companyId")
    public CompanyEntity findCompanyById(UUID companyId) {
        return companyRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found. companyId=" + companyId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "companies", key = "#companyId")
    public CompanyEntity findValidCompanyById(UUID companyId) {
        return companyRepository.findByCompanyIdAndIsDeletedFalse(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found. companyId=" + companyId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "companies", key = "#companyId")
    public CompanyResponseDto getCompanyById(UUID companyId) {
        CompanyEntity companyEntity = findCompanyById(companyId);
        return CompanyResponseDto.fromCompany(companyEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "companies", key = "#companyId")
    public CompanyResponseDto getValidCompanyById(UUID companyId) {
        CompanyEntity companyEntity = findValidCompanyById(companyId);
        return CompanyResponseDto.fromCompany(companyEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allCompanies", key = "#pageable")
    public Page<CompanyResponseDto> getAllCompanies(Pageable pageable) {
        Page<CompanyEntity> companies = companyRepository.findAllByIsDeletedFalse(pageable);
        return companies.map(CompanyResponseDto::fromCompany);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "companyList", key = "#condition")
    public List<CompanyResponseDto> getCompanyList(CompanySearchCondition condition) {
        List<CompanyEntity> companyList = companyRepository.findCompanyList(condition);
        return companyList.stream()
                .map(CompanyResponseDto::fromCompany)
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allCompanies", key = "#condition")
    public Page<CompanyResponseDto> searchCompanies(CompanySearchCondition condition, Pageable pageable) {
        Page<CompanyEntity> companies = companyRepository.search(condition, pageable);
        return companies.map(CompanyResponseDto::fromCompany);
    }

    @CacheEvict(value = "companies", key = "#companyId")
    public void updateCompany(UUID companyId, CompanyDto companyDto) {
        CompanyEntity companyEntity = findValidCompanyById(companyId);

        companyEntity.updateCompanyInfo(
                companyDto.getCompanyName()
                , companyDto.getHubId()
                , companyDto.getCompanyManagerId()
                , companyDto.getZipCode()
                , companyDto.getAddress1()
                , companyDto.getAddress2()
        );
        log.debug("updateCompany companyId:{}", companyId);
    }

    @CacheEvict(value = "companies", key = "#companyId")
    public void changeHub(UUID companyId, UUID newHubId) {
        CompanyEntity companyEntity = findValidCompanyById(companyId);
        companyEntity.updateHubId(newHubId);
        log.debug("changeHub companyId: {}, newHubId: {}", companyId, newHubId);
    }

    @CacheEvict(value = "companies", key = "#companyId")
    public void changeCompanyManager(UUID companyId, UUID newCompanyManagerId) {
        CompanyEntity companyEntity = findValidCompanyById(companyId);
        companyEntity.updateCompanyManagerId(newCompanyManagerId);
        log.debug("changeCompanyManager companyId: {}, newCompanyManagerId: {}", companyId, newCompanyManagerId);
    }

    @CacheEvict(value = "companies", key = "#companyId")
    public void changeCompanyName(UUID companyId, String newCompanyName) {
        CompanyEntity companyEntity = findValidCompanyById(companyId);
        companyEntity.updateCompanyName(newCompanyName);
        log.debug("changeCompanyName companyId: {}, newCompanyName: {}", companyId, newCompanyName);
    }

    @CacheEvict(value = "companies", key = "#companyId")
    public void changeCompanyAddress(UUID companyId, String zipCode, String address1, String address2) {
        CompanyEntity companyEntity = findValidCompanyById(companyId);
        companyEntity.updateCompanyAddress(zipCode, address1, address2);
        log.debug("changeCompanyAddress companyId: {}, zipCode: {}, address1: {}, address2: {}", companyId, zipCode, address1, address2);
    }

    @CacheEvict(value = "companies", key = "#companyId")
    public void deleteCompany(UUID companyId) {
        CompanyEntity companyEntity = findValidCompanyById(companyId);
        companyEntity.setDeleted(true);
        log.debug("deleteCompany companyId:{}", companyId);
    }

    public void validateCompanyOwner(UUID companyManagerId, UUID userId) {
        if (!companyManagerId.equals(userId)) {
            throw new AccessDeniedException(String.format("본인의 회사만 수정 가능합니다. companyManagerId: %s, userId: %s", companyManagerId, userId));
        }
    }
}
