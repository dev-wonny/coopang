package com.coopang.hub.domain.repository.hub;

import com.coopang.hub.application.request.hub.HubSearchConditionDto;
import com.coopang.hub.domain.entity.hub.HubEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    Optional<HubEntity> findByHubId(UUID hubId);

    Optional<HubEntity> findByHubIdAndIsDeletedFalse(UUID hubId);

    List<HubEntity> findHubList(HubSearchConditionDto condition);

    Page<HubEntity> search(HubSearchConditionDto condition, Pageable pageable);
}