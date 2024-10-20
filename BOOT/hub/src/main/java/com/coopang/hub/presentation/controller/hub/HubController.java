package com.coopang.hub.presentation.controller.hub;

import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.hub.application.request.hub.HubDto;
import com.coopang.hub.application.request.hub.HubSearchConditionDto;
import com.coopang.hub.application.response.hub.HubResponseDto;
import com.coopang.hub.application.service.hub.HubService;
import com.coopang.hub.presentation.request.hub.CreateHubRequestDto;
import com.coopang.hub.presentation.request.hub.HubSearchConditionRequestDto;
import com.coopang.hub.presentation.request.hub.UpdateHubRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "HubController API", description = "HubController API")
@Slf4j(topic = "HubController")
@RestController
@RequestMapping("/hubs/v1/hub")
public class HubController {
    private final HubService hubService;
    private final ModelMapperConfig mapperConfig;

    public HubController(HubService hubService, ModelMapperConfig mapperConfig) {
        this.hubService = hubService;
        this.mapperConfig = mapperConfig;
    }

    /**
     * 허브 생성
     *
     * @param req
     * @return
     */
    @Secured(UserRoleEnum.Authority.MASTER)
    @PostMapping
    public ResponseEntity<HubResponseDto> createHub(@Valid @RequestBody CreateHubRequestDto req) {
        final HubDto hubDto = mapperConfig.strictMapper().map(req, HubDto.class);
        final HubResponseDto hub = hubService.createHub(hubDto);
        return new ResponseEntity<>(hub, HttpStatus.CREATED);
    }

    //단일 허브 조회
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY, UserRoleEnum.Authority.SHIPPER})
    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> getHubById(@PathVariable UUID hubId, @RequestHeader(HEADER_USER_ROLE) String roleHeader) {
        HubResponseDto hubInfo;
        if (UserRoleEnum.isMaster(roleHeader)) {
            hubInfo = hubService.getHubById(hubId);
        } else {
            hubInfo = hubService.getValidHubById(hubId);
        }
        return new ResponseEntity<>(hubInfo, HttpStatus.OK);
    }

    //허브 검색 (페이징, 정렬, 키워드 검색)
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY, UserRoleEnum.Authority.SHIPPER})
    @GetMapping("/search")
    public ResponseEntity<Page<HubResponseDto>> searchHubs(
        @RequestHeader(HEADER_USER_ROLE) String roleHeader
        , @RequestParam(value = "hubId", required = false) UUID hubId
        , @RequestParam(value = "hubName", required = false) String hubName
        , @RequestParam(value = "hubManagerId", required = false) UUID hubManagerId
        , @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted
        , Pageable pageable
    ) {
        HubSearchConditionDto condition = HubSearchConditionDto.from(
            hubId
            , hubName
            , hubManagerId
            , isDeleted
        );

        // 마스터 권한이면 클라이언트에서 받은 isDeleted 값을 사용하고, 그외 권한은 삭제되지 않은 항목만 조회하도록 false로 설정
        if (!UserRoleEnum.isMaster(roleHeader)) {
            condition.setIsDeletedFalse();
        }

        final Page<HubResponseDto> hubs = hubService.searchHubs(condition, pageable);
        return new ResponseEntity<>(hubs, HttpStatus.OK);
    }

    /**
     * 서버에서 List 조회
     *
     * @param req
     * @return 검색된 List
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping("/list")
    public ResponseEntity<List<HubResponseDto>> getHubList(@RequestBody(required = false) HubSearchConditionRequestDto req) {
        HubSearchConditionDto condition;
        if (ObjectUtils.isEmpty(req)) {
            condition = HubSearchConditionDto.empty();
        } else {
            condition = HubSearchConditionDto.from(
                req.getHubId()
                , req.getHubName()
                , req.getHubManagerId()
                , req.isDeleted()
            );
        }
        final List<HubResponseDto> hubs = hubService.getHubList(condition);
        return new ResponseEntity<>(hubs, HttpStatus.OK);
    }

    //단일 허브 수정
    @Secured(UserRoleEnum.Authority.MASTER)
    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> updateHub(@PathVariable UUID hubId, @Valid @RequestBody UpdateHubRequestDto req) {
        final HubDto hubDto = mapperConfig.strictMapper().map(req, HubDto.class);
        hubService.updateHub(hubId, hubDto);
        final HubResponseDto hubInfo = hubService.getHubById(hubId);
        return new ResponseEntity<>(hubInfo, HttpStatus.OK);
    }

    //단일 허브 삭제 (논리적 삭제)
    @Secured(UserRoleEnum.Authority.MASTER)
    @DeleteMapping("/{hubId}")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}