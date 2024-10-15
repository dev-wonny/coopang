package com.coopang.ainoti.presentation.controller;

import com.coopang.ainoti.application.request.SlackMessageDto;
import com.coopang.ainoti.application.service.SlackNotificationService;
import com.coopang.ainoti.presentation.request.CreateSlackNotificationRequestDto;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import com.coopang.ainoti.presentation.request.UpdateSlackNotificationRequestDto;
import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.user.enums.UserRoleEnum.Authority;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slack-messages/v1/message")
public class SlackNotificationController {

    private final ModelMapperConfig mapperConfig;
    private final SlackNotificationService slackNotificationService;

    //슬랙 메시지 생성
    @Secured(Authority.MASTER)
    @PostMapping
    public ResponseEntity<?> createSlackMessage(@Valid @RequestBody CreateSlackNotificationRequestDto createSlackNotificationRequestDto){
        SlackMessageDto dto = mapperConfig.strictMapper().map(createSlackNotificationRequestDto, SlackMessageDto.class);

        return new ResponseEntity<>(slackNotificationService.createMessage(dto),HttpStatus.CREATED);
    }

    //슬랙 메시지 단일 조회
    @GetMapping("/{slackMessageId}")
    public ResponseEntity<?> getSlackMessageById(@PathVariable UUID slackMessageId){

        return new ResponseEntity<>(slackNotificationService.getSlackMessage(slackMessageId),HttpStatus.OK);
    }

    //슬랙 메시지 목록 조회
    @GetMapping
    public ResponseEntity<?> getSlackMessages(@PageableDefault(size = 10) Pageable pageable){

        return new ResponseEntity<>(slackNotificationService.getSlackMessagesWithPageable(pageable),HttpStatus.OK);
    }

    //슬랙 메시지 목록 조회 - 키워드검색
    @PostMapping("/search")
    public ResponseEntity<?> getSlackMessagesWithCondition(@RequestBody SlackMessageSearchConditionDto conditionDto,Pageable pageable){

        return new ResponseEntity<>(
            slackNotificationService.searchWithCondition(conditionDto,pageable),HttpStatus.OK);
    }

    //슬랙 메시지 수정
    @Secured(Authority.MASTER)
    @PutMapping("/{slackMessageId}")
    public ResponseEntity<?> UpdateSlackMessageById(@RequestBody UpdateSlackNotificationRequestDto updateSlackNotificationRequestDto,@PathVariable UUID slackMessageId){

        SlackMessageDto slackMessageDto = mapperConfig.strictMapper().map(
            updateSlackNotificationRequestDto, SlackMessageDto.class);
        slackNotificationService.updateMessage(updateSlackNotificationRequestDto,slackMessageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //슬랙 메시지 삭제
    @Secured(Authority.MASTER)
    @DeleteMapping("/{slackMessageId}")
    public ResponseEntity<?> deleteSlackMessageById(@PathVariable UUID slackMessageId){
        slackNotificationService.deleteMessage(slackMessageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //슬랙메시지 보내는 것(테스트용)
    @PostMapping("/send-slack-message")
    public String sendSlackMessage(@RequestParam String slackId, @RequestParam String message) {
        slackNotificationService.sendSlackMessage(slackId,message);
        return "Message sent to Slack!";
    }
}
