package com.coopang.hub.infrastructure.repository.hub;

import com.coopang.hub.application.request.hub.HubSearchConditionDto;
import com.coopang.hub.domain.entity.hub.HubEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HubRepositoryCustom {
    Page<HubEntity> search(HubSearchConditionDto condition, Pageable pageable);
    List<HubEntity> findHubList(HubSearchConditionDto condition);

}
