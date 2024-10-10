package com.coopang.hub.domain.repository.hub;

import com.coopang.hub.application.request.hub.HubSearchCondition;
import com.coopang.hub.domain.entity.hub.HubEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    Optional<HubEntity> findByHubId(UUID hubId);

    Optional<HubEntity> findByHubIdAndIsDeletedFalse(UUID hubId);

    List<HubEntity> findHubList(HubSearchCondition condition);

    Page<HubEntity> search(HubSearchCondition condition, Pageable pageable);
}