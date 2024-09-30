package com.coopang.hub.infrastructure.repository;

import com.coopang.hub.domain.entity.hub.HubEntity;
import com.coopang.hub.presentation.request.HubSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRepositoryCustom {
    Page<HubEntity> search(HubSearchCondition condition, Pageable pageable);
}
