package com.coopang.hub.application.service.shipper;

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.hub.application.request.shipper.ShipperDto;
import com.coopang.hub.application.request.shipper.ShipperSearchConditionDto;
import com.coopang.hub.application.response.shipper.ShipperResponseDto;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import com.coopang.hub.domain.repository.shipper.ShipperRepository;
import com.coopang.hub.domain.service.shipper.ShipperDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j(topic = "ShipperService")
@Transactional
@Service
public class ShipperService {
    private final ShipperRepository shipperRepository;
    private final ShipperDomainService shipperDomainService;

    public ShipperService(ShipperRepository shipperRepository, ShipperDomainService shipperDomainService) {
        this.shipperRepository = shipperRepository;
        this.shipperDomainService = shipperDomainService;
    }

    @Transactional
    public ShipperResponseDto createShipper(ShipperDto shipperDto) {
        // 서비스 레이어에서 UUID 생성
        final UUID userId = shipperDto.getShipperId() != null ? shipperDto.getShipperId() : UUID.randomUUID();
        shipperDto.setShipperId(userId);

        ShipperEntity shipperEntity = shipperDomainService.createShipper(shipperDto);
        return ShipperResponseDto.fromShipper(shipperEntity);
    }

    // ADMIN 전용
    @Transactional(readOnly = true)
    @Cacheable(value = "shippers", key = "#shipperId")
    public ShipperEntity findShipperById(UUID shipperId) {
        return shipperRepository.findByShipperId(shipperId)
            .orElseThrow(() -> new IllegalArgumentException("Shipper not found. shipperId=" + shipperId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shippers", key = "#shipperId")
    public ShipperEntity findValidShipperById(UUID shipperId) {
        return shipperRepository.findByShipperIdAndIsDeletedFalse(shipperId)
            .orElseThrow(() -> new IllegalArgumentException("Shipper not found. shipperId=" + shipperId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shippers", key = "#shipperId")
    public ShipperResponseDto getShipperById(UUID shipperId) {
        ShipperEntity shipperEntity = findShipperById(shipperId);
        return ShipperResponseDto.fromShipper(shipperEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shippers", key = "#shipperId")
    public ShipperResponseDto getValidShipperById(UUID shipperId) {
        ShipperEntity shipperEntity = findValidShipperById(shipperId);
        return ShipperResponseDto.fromShipper(shipperEntity);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allShippers", key = "#pageable")
    public Page<ShipperResponseDto> getAllShippers(Pageable pageable) {
        final Page<ShipperEntity> shippers = shipperRepository.findAll(pageable);
        return shippers.map(ShipperResponseDto::fromShipper);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "shipperList", key = "#condition")
    public List<ShipperResponseDto> getShipperList(ShipperSearchConditionDto condition) {
        final List<ShipperEntity> shippers = shipperRepository.findShipperList(condition);
        return shippers.stream()
            .map(ShipperResponseDto::fromShipper)
            .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "allShippers", key = "#condition.toString() + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<ShipperResponseDto> searchShippers(ShipperSearchConditionDto condition, Pageable pageable) {
        final Page<ShipperEntity> shippers = shipperRepository.search(condition, pageable);
        return shippers.map(ShipperResponseDto::fromShipper);
    }

    @CacheEvict(value = "shippers", key = "#shipperId")
    public void updateShipperInfo(UUID shipperId, ShipperDto shipperDto) {
        ShipperEntity shipperEntity = findValidShipperById(shipperId);
        shipperEntity.updateShipperInfo(shipperDto.getHubId(), shipperDto.getShipperType());
        log.debug("updateShipperInfo shipperId:{}", shipperId);
    }

    @CacheEvict(value = "shippers", key = "#shipperId")
    public void updateShipperInfo(ShipperEntity shipperEntity, ShipperDto shipperDto) {
        shipperEntity.updateShipperInfo(shipperDto.getHubId(), shipperDto.getShipperType());
        log.debug("updateShipperInfo shipperId:{}", shipperEntity.getShipperId());
    }

    @CacheEvict(value = "shippers", key = "#shipperId")
    public void changeHub(UUID shipperId, UUID hubId) {
        ShipperEntity shipperEntity = findValidShipperById(shipperId);
        shipperEntity.updateHubId(hubId);
        log.debug("changeHub shipperId:{}", shipperId);
    }

    @CacheEvict(value = "shippers", key = "#shipperId")
    public void changeShipperType(UUID shipperId, ShipperTypeEnum shipperType) {
        ShipperEntity shipperEntity = findValidShipperById(shipperId);
        shipperEntity.updateShipperType(shipperType);
        log.debug("changeShipperType shipperId:{}", shipperId);
    }

    @CacheEvict(value = "shippers", key = "#shipperId")
    public void deleteShipper(UUID shipperId) {
        ShipperEntity shipperEntity = findValidShipperById(shipperId);
        shipperEntity.setDeleted(true);
        log.debug("deleteShipper shipperId:{}", shipperId);
    }
}
