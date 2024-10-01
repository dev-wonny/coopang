package com.coopang.hub.application.service;

import com.coopang.hub.application.request.HubDto;
import com.coopang.hub.application.response.HubResponseDto;
import com.coopang.hub.domain.entity.hub.HubEntity;
import com.coopang.hub.domain.repository.HubRepository;
import com.coopang.hub.domain.service.HubDomainService;
import com.coopang.hub.presentation.request.HubSearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j(topic = "HubService")
@Service
@Transactional
public class HubService {

    private final HubRepository hubRepository;
    private final HubDomainService hubDomainService;

    public HubService(HubRepository hubRepository, HubDomainService hubDomainService) {
        this.hubRepository = hubRepository;
        this.hubDomainService = hubDomainService;
    }

    public HubResponseDto createHub(HubDto hubDto) {
        HubEntity hubEntity = hubDomainService.createHub(hubDto);
        return HubResponseDto.fromHub(hubEntity);
    }

    public HubResponseDto getHubById(UUID hubId) {
        HubEntity hubEntity = findByHubId(hubId);
        return HubResponseDto.fromHub(hubEntity);
    }

    public Page<HubResponseDto> getAllHubs(Pageable pageable) {
        Page<HubEntity> hubs = hubRepository.findAllByDeletedFalse(pageable);
        return hubs.map(HubResponseDto::fromHub);
    }

    public Page<HubResponseDto> searchHubs(HubSearchCondition condition, Pageable pageable) {
        Page<HubEntity> hubs = hubRepository.search(condition, pageable);
        return hubs.map(HubResponseDto::fromHub);
    }

    public HubEntity findByHubId(UUID hubId) {
        return hubRepository.findByHubId(hubId).orElseThrow(() -> new IllegalArgumentException("Hub not found. hubId=" + hubId));
    }

    public void updateHub(UUID hubId, HubDto hubDto) {
        HubEntity hubEntity = findByHubId(hubId);
        hubEntity.updateHubInfo(hubDto.getHubName(), hubDto.getHubManagerId(), hubDto.getZipCode(), hubDto.getAddress1(), hubDto.getAddress2());
        log.debug("updateHub hubId:{}", hubId);
    }

    public void deleteHub(UUID hubId) {
        HubEntity hubEntity = findByHubId(hubId);
        hubEntity.setDeleted(true);
        log.debug("deleteHub hubId:{}", hubId);
    }
}
