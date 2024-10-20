package com.coopang.ainoti.presentation.controller.noti;


import com.coopang.ainoti.application.request.noti.SlackMessageDto;
import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.application.response.noti.SlackMessageResponseDto;
import com.coopang.ainoti.application.service.noti.SlackMessageService;
import com.coopang.ainoti.presentation.request.noti.CreateSlackMessageRequestDto;
import com.coopang.ainoti.presentation.request.noti.SlackMessageSearchConditionRequestDto;
import com.coopang.ainoti.presentation.request.noti.UpdateSlackMessageRequestDto;
import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.coredata.user.enums.UserRoleEnum;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "SlackMessageController API", description = "Slack 메시지 관리 API")
@Slf4j(topic = "SlackMessageController")
@RestController
@RequestMapping("/slack-messages/v1/slack-message")
public class SlackMessageController {

    private final SlackMessageService slackMessageService;
    private final ModelMapperConfig mapperConfig;

    public SlackMessageController(SlackMessageService slackMessageService, ModelMapperConfig mapperConfig) {
        this.slackMessageService = slackMessageService;
        this.mapperConfig = mapperConfig;
    }

    /**
     * Slack 메시지 생성
     *
     * @param req
     * @return
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping
    public ResponseEntity<SlackMessageResponseDto> createSlackMessage(@Valid @RequestBody CreateSlackMessageRequestDto req) {
        final SlackMessageDto slackMessageDto = mapperConfig.strictMapper().map(req, SlackMessageDto.class);
        final SlackMessageResponseDto response = slackMessageService.createSlackMessage(slackMessageDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 단일 Slack 메시지 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @GetMapping("/{slackMessageId}")
    public ResponseEntity<SlackMessageResponseDto> getSlackMessageById(@PathVariable UUID slackMessageId) {
        SlackMessageResponseDto slackMessage = slackMessageService.getValidSlackMessageById(slackMessageId);
        return new ResponseEntity<>(slackMessage, HttpStatus.OK);
    }

    /**
     * Slack 메시지 리스트 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping("/list")
    public ResponseEntity<List<SlackMessageResponseDto>> getSlackMessageList(@RequestBody SlackMessageSearchConditionRequestDto req) {
        final SlackMessageSearchConditionDto condition = mapperConfig.strictMapper().map(req, SlackMessageSearchConditionDto.class);
        final List<SlackMessageResponseDto> responseList = slackMessageService.getSlackMessageList(condition);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    /**
     * Slack 메시지 검색 (페이징, 정렬, 키워드 검색)
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping("/search")
    public ResponseEntity<Page<SlackMessageResponseDto>> searchSlackMessages(@RequestBody SlackMessageSearchConditionRequestDto req,
                                                                             Pageable pageable) {
        final SlackMessageSearchConditionDto condition = mapperConfig.strictMapper().map(req, SlackMessageSearchConditionDto.class);
        Page<SlackMessageResponseDto> responsePage = slackMessageService.searchSlackMessages(condition, pageable);
        return new ResponseEntity<>(responsePage, HttpStatus.OK);
    }

    /**
     * Slack 메시지 수정
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PutMapping("/{slackMessageId}")
    public ResponseEntity<SlackMessageResponseDto> updateSlackMessage(@PathVariable UUID slackMessageId,
                                                                      @Valid @RequestBody UpdateSlackMessageRequestDto req) {
        final SlackMessageDto slackMessageDto = mapperConfig.strictMapper().map(req, SlackMessageDto.class);
        slackMessageService.updateSlackMessage(slackMessageId, slackMessageDto);
        final SlackMessageResponseDto slackMessage = slackMessageService.getSlackMessageById(slackMessageId);
        return new ResponseEntity<>(slackMessage, HttpStatus.OK);
    }

    /**
     * 단일 Slack 메시지 삭제 (논리적 삭제)
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @DeleteMapping("/{slackMessageId}")
    public ResponseEntity<Void> deleteSlackMessage(@PathVariable UUID slackMessageId) {
        slackMessageService.deleteSlackMessage(slackMessageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
