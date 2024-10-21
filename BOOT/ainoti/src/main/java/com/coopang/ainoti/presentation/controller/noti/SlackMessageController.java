package com.coopang.ainoti.presentation.controller.noti;


import com.coopang.ainoti.application.enums.SlackMessageStatus;
import com.coopang.ainoti.application.request.noti.SlackMessageDto;
import com.coopang.ainoti.application.request.noti.SlackMessageSearchConditionDto;
import com.coopang.ainoti.application.response.noti.SlackMessageResponseDto;
import com.coopang.ainoti.application.service.noti.SlackMessageService;
import com.coopang.ainoti.presentation.request.noti.CreateSlackMessageRequestDto;
import com.coopang.ainoti.presentation.request.noti.SlackMessageSearchConditionRequestDto;
import com.coopang.ainoti.presentation.request.noti.UpdateSlackMessageRequestDto;
import com.coopang.apiconfig.datetime.DateTimeUtil;
import com.coopang.coredata.user.enums.UserRoleEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "SlackMessageController API", description = "Slack 메시지 관리 API")
@Slf4j(topic = "SlackMessageController")
@RestController
@RequestMapping("/slack-messages/v1/slack-message")
public class SlackMessageController {
    private final SlackMessageService slackMessageService;

    public SlackMessageController(SlackMessageService slackMessageService) {
        this.slackMessageService = slackMessageService;
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
        SlackMessageDto slackMessageDto = SlackMessageDto.from(
            null
            , req.getReceiveSlackId()
            , req.getReceiveUserId()// 선택적 필드
            , !StringUtils.isBlank(req.getSlackMessageStatus()) ? SlackMessageStatus.getStatusEnum(req.getSlackMessageStatus()) : null
            , req.getSlackMessage()
            , DateTimeUtil.parseToLocalDateTime(req.getSentTime())
            , req.getSlackMessageSenderId()// 선택적 필드
        );
        final SlackMessageResponseDto slackMessage = slackMessageService.createSlackMessage(slackMessageDto);
        return new ResponseEntity<>(slackMessage, HttpStatus.CREATED);
    }

    /**
     * 단일 Slack 메시지 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @GetMapping("/{slackMessageId}")
    public ResponseEntity<SlackMessageResponseDto> getSlackMessageById(@PathVariable UUID slackMessageId) {
        final SlackMessageResponseDto slackMessage = slackMessageService.getValidSlackMessageById(slackMessageId);
        return new ResponseEntity<>(slackMessage, HttpStatus.OK);
    }

    /**
     * Slack 메시지 검색 (페이징, 정렬, 키워드 검색)
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @GetMapping("/search")
    public ResponseEntity<Page<SlackMessageResponseDto>> searchSlackMessages(
        @RequestParam(value = "slackMessageId", required = false) UUID slackMessageId
        , @RequestParam(value = "receiveSlackId", required = false) String receiveSlackId
        , @RequestParam(value = "receiveUserId", required = false) UUID receiveUserId
        , @RequestParam(value = "slackMessageStatus", required = false) String slackMessageStatus
        , @RequestParam(value = "slackMessage", required = false) String slackMessage
        , @RequestParam(value = "sentTimeFrom", required = false) String sentTimeFrom
        , @RequestParam(value = "sentTimeTo", required = false) String sentTimeTo
        , @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted
        , Pageable pageable) {

        final SlackMessageSearchConditionDto condition = SlackMessageSearchConditionDto.from(
            slackMessageId
            , receiveSlackId
            , receiveUserId
            , !StringUtils.isBlank(slackMessageStatus) ? SlackMessageStatus.getStatusEnum(slackMessageStatus) : null
            , slackMessage
            , DateTimeUtil.parseToLocalDateTime(sentTimeFrom)
            , DateTimeUtil.parseToLocalDateTime(sentTimeTo)
            , isDeleted
        );

        final Page<SlackMessageResponseDto> slackMessages = slackMessageService.searchSlackMessages(condition, pageable);
        return new ResponseEntity<>(slackMessages, HttpStatus.OK);
    }

    /**
     * Slack 메시지 리스트 조회
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PostMapping("/list")
    public ResponseEntity<List<SlackMessageResponseDto>> getSlackMessageList(@RequestBody(required = false) SlackMessageSearchConditionRequestDto req) {
        SlackMessageSearchConditionDto condition;
        if (ObjectUtils.isEmpty(req)) {
            condition = SlackMessageSearchConditionDto.empty();
        } else {
            condition = SlackMessageSearchConditionDto.from(
                req.getSlackMessageId()
                , req.getReceiveSlackId()
                , req.getReceiveUserId()
                , !StringUtils.isBlank(req.getSlackMessageStatus()) ? SlackMessageStatus.getStatusEnum(req.getSlackMessageStatus()) : null
                , req.getSlackMessage()
                , DateTimeUtil.parseToLocalDateTime(req.getSentTimeFrom())
                , DateTimeUtil.parseToLocalDateTime(req.getSentTimeTo())
                , req.isDeleted()
            );
        }
        final List<SlackMessageResponseDto> slackMessageList = slackMessageService.getSlackMessageList(condition);
        return new ResponseEntity<>(slackMessageList, HttpStatus.OK);
    }

    /**
     * Slack 메시지 수정
     */
    @Secured({UserRoleEnum.Authority.SERVER, UserRoleEnum.Authority.MASTER})
    @PutMapping("/{slackMessageId}")
    public ResponseEntity<SlackMessageResponseDto> updateSlackMessage(@PathVariable UUID slackMessageId, @Valid @RequestBody UpdateSlackMessageRequestDto req) {
        final SlackMessageDto slackMessageDto = SlackMessageDto.from(
            slackMessageId
            , req.getReceiveSlackId()
            , req.getReceiveUserId()// 선택적 필드
            , !StringUtils.isBlank(req.getSlackMessageStatus()) ? SlackMessageStatus.getStatusEnum(req.getSlackMessageStatus()) : null
            , req.getSlackMessage()
            , DateTimeUtil.parseToLocalDateTime(req.getSentTime())
            , req.getSlackMessageSenderId()// 선택적 필드
        );
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
