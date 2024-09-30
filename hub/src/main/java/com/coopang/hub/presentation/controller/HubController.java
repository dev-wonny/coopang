package com.coopang.hub.presentation.controller;

import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.user.response.UserResponse;
import com.coopang.hub.application.request.HubDto;
import com.coopang.hub.application.response.HubResponseDto;
import com.coopang.hub.application.service.HubService;
import com.coopang.hub.application.service.UserService;
import com.coopang.hub.presentation.request.HubRequestDto;
import com.coopang.hub.presentation.request.HubSearchCondition;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "HubController API", description = "HubController API")
@Slf4j(topic = "HubController")
@RestController
@RequestMapping("/hub/v1/hubs")
public class HubController {

    private final HubService hubService;
    private final UserService userService;
    private final ModelMapperConfig mapperConfig;

    public HubController(HubService hubService, UserService userService, ModelMapperConfig mapperConfig) {
        this.hubService = hubService;
        this.userService = userService;
        this.mapperConfig = mapperConfig;
    }

    @Secured("ROLE_MASTER")
    @PostMapping
    public ResponseEntity<HubResponseDto> createHub(@Valid @RequestBody HubRequestDto hubRequestDto) {
        HubDto hubDto = mapperConfig.strictMapper().map(hubRequestDto, HubDto.class);
        HubResponseDto hub = hubService.createHub(hubDto);
        return new ResponseEntity<>(hub, HttpStatus.CREATED);
    }

    @Secured({"ROLE_MASTER", "ROLE_USER"})
    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> getHubById(@PathVariable UUID hubId) {
        HubResponseDto hubInfo = hubService.getHubById(hubId);
        return new ResponseEntity<>(hubInfo, HttpStatus.OK);
    }

    @Secured({"ROLE_MASTER", "ROLE_USER"})
    @GetMapping
    public ResponseEntity<Page<HubResponseDto>> getAllHubs(Pageable pageable) {
        Page<HubResponseDto> hubs = hubService.getAllHubs(pageable);
        return new ResponseEntity<>(hubs, HttpStatus.OK);
    }

    @Secured({"ROLE_MASTER", "ROLE_USER"})
    @GetMapping("/search")
    public ResponseEntity<Page<HubResponseDto>> searchHubs(HubSearchCondition condition, Pageable pageable) {
        Page<HubResponseDto> hubs = hubService.searchHubs(condition, pageable);
        return new ResponseEntity<>(hubs, HttpStatus.OK);
    }

    @Secured("ROLE_MASTER")
    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> updateHub(@PathVariable UUID hubId, @Valid @RequestBody HubRequestDto hubRequestDto) {
        HubDto hubDto = mapperConfig.strictMapper().map(hubRequestDto, HubDto.class);
        hubService.updateHub(hubId, hubDto);
        final HubResponseDto hubInfo = hubService.getHubById(hubId);
        return new ResponseEntity<>(hubInfo, HttpStatus.OK);
    }

    @Secured("ROLE_MASTER")
    @DeleteMapping("/{hubId}")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/test/{userId}")
    public ResponseEntity<UserResponse> test(@PathVariable UUID userId) {
        return new ResponseEntity<>(userService.getUserInfo(userId), HttpStatus.OK);
    }
}