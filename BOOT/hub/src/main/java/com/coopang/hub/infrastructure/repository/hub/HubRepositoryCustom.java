package com.coopang.hub.infrastructure.repository.hub;

import com.coopang.hub.application.request.hub.HubSearchCondition;
import com.coopang.hub.domain.entity.hub.HubEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HubRepositoryCustom {
    Page<HubEntity> search(HubSearchCondition condition, Pageable pageable);
    List<HubEntity> findHubList(HubSearchCondition condition);

}
