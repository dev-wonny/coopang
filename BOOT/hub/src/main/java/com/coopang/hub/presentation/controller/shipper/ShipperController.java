package com.coopang.hub.presentation.controller.shipper;


import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;
import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.hub.application.request.shipper.ShipperDto;
import com.coopang.hub.application.request.shipper.ShipperSearchCondition;
import com.coopang.hub.application.response.shipper.ShipperResponseDto;
import com.coopang.hub.application.service.hub.HubService;
import com.coopang.hub.application.service.shipper.ShipperService;
import com.coopang.hub.application.validator.HubPermissionValidator;
import com.coopang.hub.domain.entity.shipper.ShipperEntity;
import com.coopang.hub.presentation.request.shipper.CreateShipperRequestDto;
import com.coopang.hub.presentation.request.shipper.HubIdRequestDto;
import com.coopang.hub.presentation.request.shipper.ShipperSearchConditionRequestDto;
import com.coopang.hub.presentation.request.shipper.ShipperTypeRequestDto;
import com.coopang.hub.presentation.request.shipper.UpdateShipperRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "ShipperController API", description = "ShipperController API")
@Slf4j(topic = "ShipperController")
@RestController
@RequestMapping("/shippers/v1/shipper")
public class ShipperController {

    private final ShipperService shipperService;
    private final ModelMapperConfig mapperConfig;
    private final HubService hubService;

    public ShipperController(ShipperService shipperService, ModelMapperConfig mapperConfig, HubService hubService) {
        this.shipperService = shipperService;
        this.mapperConfig = mapperConfig;
        this.hubService = hubService;
    }

    /**
     * 생성
     * 마스터: 제한 없음
     * 허브매니저: 소속 허브가 같으면 생성 가능
     *
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    @PostMapping
    public ResponseEntity<ShipperResponseDto> createShipper(
            @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @Valid @RequestBody CreateShipperRequestDto req
    ) {
        HubPermissionValidator.validateHubManagerBelongToHub(roleHeader, req.getHubId(), UUID.fromString(userIdHeader), hubService);
        final ShipperDto shipperDto = mapperConfig.strictMapper().map(req, ShipperDto.class);
        ShipperResponseDto shipper = shipperService.createShipper(shipperDto);
        return new ResponseEntity<>(shipper, HttpStatus.CREATED);
    }

    /**
     * 조회
     * 관리자: 제한 없음
     *
     * @param shipperId
     * @param userIdHeader
     * @param roleHeader
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.SHIPPER})
    @GetMapping("/{shipperId}")
    public ResponseEntity<ShipperResponseDto> getShipperById(
            @PathVariable UUID shipperId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        ShipperResponseDto shipper;

        // 마스터면 delete도 보임, 그외 권한 delete 안 보임
        if (UserRoleEnum.isMaster(roleHeader)) {
            shipper = shipperService.getShipperById(shipperId);
        } else {
            shipper = shipperService.getValidShipperById(shipperId);
        }
        return new ResponseEntity<>(shipper, HttpStatus.OK);
    }

    /**
     * 마스터 백오피스
     * 전체 조회
     *
     * @param pageable
     * @return
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @GetMapping
    public ResponseEntity<Page<ShipperResponseDto>> getAllShippers(Pageable pageable) {
        Page<ShipperResponseDto> shippers = shipperService.getAllShippers(pageable);
        return new ResponseEntity<>(shippers, HttpStatus.OK);
    }

    /**
     * 운영진 백오피스
     * 검색
     *
     * @param roleHeader
     * @param req
     * @param pageable
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.SHIPPER})
    @PostMapping("/search")
    public ResponseEntity<Page<ShipperResponseDto>> searchShippers(
            @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @RequestBody ShipperSearchConditionRequestDto req
            , Pageable pageable
    ) {
        // 마스터 권한이면 클라이언트에서 받은 isDeleted 값을 사용하고, 그외 권한은 삭제되지 않은 항목만 조회하도록 false로 설정
        final boolean isDeleted = UserRoleEnum.isMaster(roleHeader) ? req.getIsDeleted() : false;
        final ShipperSearchCondition condition = ShipperSearchCondition.from(
                req.getShipperId()
                , req.getHubId()
                , req.getShipperType()
                , req.getHubName()
                , isDeleted
        );

        Page<ShipperResponseDto> shippers = shipperService.searchShippers(condition, pageable);
        return new ResponseEntity<>(shippers, HttpStatus.OK);
    }

    @Secured(UserRoleEnum.Authority.SERVER)
    @PostMapping("/list")
    public ResponseEntity<List<ShipperResponseDto>> getShipperList(@RequestBody ShipperSearchConditionRequestDto req) {
        final ShipperSearchCondition condition = ShipperSearchCondition.from(
                req.getShipperId()
                , req.getHubId()
                , req.getShipperType()
                , req.getHubName()
                , req.getIsDeleted()
        );
        List<ShipperResponseDto> shipperList = shipperService.getShipperList(condition);
        return new ResponseEntity<>(shipperList, HttpStatus.OK);
    }

    /**
     * 수정(hubId, shipperType)
     * 마스터: 제한 없음
     * 허브매니저: 소속 허브가 같으면 생성 가능
     *
     * @param shipperId
     * @param userIdHeader
     * @param roleHeader
     * @param updateShipperRequestDto
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    @PutMapping("/{shipperId}")
    public ResponseEntity<ShipperResponseDto> updateShipperInfo(
            @PathVariable UUID shipperId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @Valid @RequestBody UpdateShipperRequestDto updateShipperRequestDto
    ) {
        // 수정하려는 shipper의 이전 정보
        ShipperEntity shipperEntity = shipperService.findShipperById(shipperId);
        HubPermissionValidator.validateHubManagerBelongToHub(roleHeader, shipperEntity.getHubId(), UUID.fromString(userIdHeader), hubService);

        final ShipperDto shipperDto = mapperConfig.strictMapper().map(updateShipperRequestDto, ShipperDto.class);
        shipperService.updateShipperInfo(shipperEntity, shipperDto);

        ShipperResponseDto shipper = shipperService.getShipperById(shipperId);
        return new ResponseEntity<>(shipper, HttpStatus.OK);
    }

    /**
     * 수정 hubId
     * 마스터: 제한 없음
     * 허브매니저: 소속 허브가 같으면 생성 가능
     *
     * @param shipperId
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    @PatchMapping("/{shipperId}/change-hub")
    public ResponseEntity<Void> changeHub(
            @PathVariable UUID shipperId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @Valid @RequestBody HubIdRequestDto req
    ) {
        // 수정하려는 shipper의 이전 정보
        final ShipperResponseDto shipper = shipperService.getShipperById(shipperId);
        HubPermissionValidator.validateHubManagerBelongToHub(roleHeader, shipper.getHubId(), UUID.fromString(userIdHeader), hubService);

        shipperService.changeHub(shipperId, req.getHubId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 수정 shipperType
     * 마스터: 제한 없음
     * 허브매니저: 소속 허브가 같으면 생성 가능
     *
     * @param shipperId
     * @param userIdHeader
     * @param roleHeader
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    @PatchMapping("/{shipperId}/change-shipperType")
    public ResponseEntity<Void> changeShipperType(
            @PathVariable UUID shipperId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @Valid @RequestBody ShipperTypeRequestDto req
    ) {
        // 수정하려는 shipper의 이전 정보
        final ShipperResponseDto shipper = shipperService.getShipperById(shipperId);
        HubPermissionValidator.validateHubManagerBelongToHub(roleHeader, shipper.getHubId(), UUID.fromString(userIdHeader), hubService);

        shipperService.changeShipperType(shipperId, ShipperTypeEnum.getRoleEnum(req.getShipperType()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 삭제
     * 마스터: 제한 없음
     * 허브매니저: 소속 허브가 같으면 생성 가능
     *
     * @param shipperId
     * @param userIdHeader
     * @param roleHeader
     * @return
     */
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    @DeleteMapping("/{shipperId}")
    public ResponseEntity<Void> deleteShipper(
            @PathVariable UUID shipperId
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        // 수정하려는 shipper의 이전 정보
        final ShipperResponseDto shipper = shipperService.getShipperById(shipperId);
        HubPermissionValidator.validateHubManagerBelongToHub(roleHeader, shipper.getHubId(), UUID.fromString(userIdHeader), hubService);

        shipperService.deleteShipper(shipperId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
