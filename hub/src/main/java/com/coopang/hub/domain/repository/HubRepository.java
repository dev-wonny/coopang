package com.coopang.hub.domain.repository;

import com.coopang.hub.domain.entity.hub.HubEntity;
import com.coopang.hub.presentation.request.HubSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    Optional<HubEntity> findByHubId(UUID hubId);

    Page<HubEntity> findAllByDeletedFalse(Pageable pageable);

    Page<HubEntity> search(HubSearchCondition condition, Pageable pageable);

}