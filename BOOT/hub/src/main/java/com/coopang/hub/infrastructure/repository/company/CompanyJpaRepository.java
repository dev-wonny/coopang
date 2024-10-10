package com.coopang.hub.infrastructure.repository.company;

import com.coopang.hub.domain.entity.company.CompanyEntity;
import com.coopang.hub.domain.repository.company.CompanyRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID>, CompanyRepository, CompanyRepositoryCustom {
}
