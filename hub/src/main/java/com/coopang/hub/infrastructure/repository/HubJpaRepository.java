package com.coopang.hub.infrastructure.repository;

import com.coopang.hub.domain.entity.hub.HubEntity;
import com.coopang.hub.domain.repository.HubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubJpaRepository extends JpaRepository<HubEntity, UUID>, HubRepository, HubRepositoryCustom {
    Optional<HubEntity> findByHubId(UUID hubId);
}
