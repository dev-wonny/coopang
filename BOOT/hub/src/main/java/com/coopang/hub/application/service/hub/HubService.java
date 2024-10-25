package com.coopang.hub.application.service.hub;

import com.coopang.hub.application.request.hub.HubDto;
import com.coopang.hub.application.request.hub.HubSearchConditionDto;
import com.coopang.hub.application.response.hub.HubResponseDto;
import com.coopang.hub.domain.entity.hub.HubEntity;
import com.coopang.hub.domain.repository.hub.HubRepository;
import com.coopang.hub.domain.service.hub.HubDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j(topic = "HubService")
@Transactional
@Service
public class HubService {
    private final HubRepository hubRepository;
    private final HubDomainService hubDomainService;

    public HubService(HubRepository hubRepository, HubDomainService hubDomainService) {
        this.hubRepository = hubRepository;
        this.hubDomainService = hubDomainService;
    }

    @Transactional
    @CacheEvict(value = {"hubs", "hubList", "allHubs"}, allEntries = true)
    public HubResponseDto createHub(HubDto hubDto) {
        // 서비스 레이어에서 UUID 생성
        final UUID hubId = hubDto.getHubId() != null ? hubDto.getHubId() : UUID.randomUUID();
        hubDto.createId(hubId);

        HubEntity hubEntity = hubDomainService.createHub(hubDto);
        return HubResponseDto.fromHub(hubEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "hubs", key = "#hubId")
    public HubEntity findValidHubById(UUID hubId) {
        return hubRepository.findByHubIdAndIsDeletedFalse(hubId).orElseThrow(() -> new IllegalArgumentException("Hub not found. hubId=" + hubId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "hubs", key = "#hubId")
    public HubEntity findHubById(UUID hubId) {
        return hubRepository.findByHubId(hubId).orElseThrow(() -> new IllegalArgumentException("Hub not found. hubId=" + hubId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "hubs", key = "#hubId")
    public HubResponseDto getHubById(UUID hubId) {
        HubEntity hubEntity = findHubById(hubId);
        return HubResponseDto.fromHub(hubEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "hubs", key = "#hubId")
    public HubResponseDto getValidHubById(UUID hubId) {
        HubEntity hubEntity = findValidHubById(hubId);
        return HubResponseDto.fromHub(hubEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "hubList", key = "#condition.toString()")
    public List<HubResponseDto> getHubList(HubSearchConditionDto condition) {
        List<HubEntity> hubs = hubRepository.findHubList(condition);
        return hubs.stream()
            .map(HubResponseDto::fromHub)
            .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allHubs", key = "#condition.toString() + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<HubResponseDto> searchHubs(HubSearchConditionDto condition, Pageable pageable) {
        Page<HubEntity> hubs = hubRepository.search(condition, pageable);
        return hubs.map(HubResponseDto::fromHub);
    }

    @CacheEvict(value = "hubs", key = "#hubId")
    public void updateHub(UUID hubId, HubDto hubDto) {
        HubEntity hubEntity = findHubById(hubId);
        hubEntity.updateHubInfo(hubDto.getHubName(), hubDto.getHubManagerId(), hubDto.getZipCode(), hubDto.getAddress1(), hubDto.getAddress2());
        log.debug("updateHub hubId:{}", hubId);
    }

    @CacheEvict(value = "hubs", key = "#hubId")
    public void deleteHub(UUID hubId) {
        HubEntity hubEntity = findHubById(hubId);
        hubEntity.setDeleted(true);
        log.debug("deleteHub hubId:{}", hubId);
    }
}
