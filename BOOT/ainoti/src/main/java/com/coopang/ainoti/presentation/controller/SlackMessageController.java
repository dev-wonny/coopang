package com.coopang.ainoti.presentation.controller;

import com.coopang.ainoti.application.request.SlackMessageDto;
import com.coopang.ainoti.application.service.SlackMessageService;
import com.coopang.ainoti.presentation.request.CreateSlackMessageRequestDto;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import com.coopang.ainoti.presentation.request.UpdateSlackMessageRequestDto;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slack-messages/v1/message")
public class SlackMessageController {

    private final ModelMapperConfig mapperConfig;
    private final SlackMessageService slackMessageService;

    //슬랙 메시지 생성
    @Secured(Authority.MASTER)
    @PostMapping
    public ResponseEntity<?> createSlackMessage(@Valid @RequestBody CreateSlackMessageRequestDto createSlackMessageRequestDto){
        SlackMessageDto dto = mapperConfig.strictMapper().map(createSlackMessageRequestDto, SlackMessageDto.class);

        return new ResponseEntity<>(slackMessageService.createMessage(dto),HttpStatus.CREATED);
    }

    //슬랙 메시지 단일 조회
    @GetMapping("/{slackMessageId}")
    public ResponseEntity<?> getSlackMessageById(@PathVariable UUID slackMessageId){

        return new ResponseEntity<>(slackMessageService.getSlackMessage(slackMessageId),HttpStatus.OK);
    }

    //슬랙 메시지 목록 조회
    @GetMapping
    public ResponseEntity<?> getSlackMessages(@PageableDefault(size = 10) Pageable pageable){

        return new ResponseEntity<>(slackMessageService.getSlackMessagesWithPageable(pageable),HttpStatus.OK);
    }

    //슬랙 메시지 목록 조회 - 키워드검색
    @PostMapping("/search")
    public ResponseEntity<?> getSlackMessagesWithCondition(@ModelAttribute SlackMessageSearchConditionDto conditionDto,Pageable pageable){

        return new ResponseEntity<>(slackMessageService.searchWithCondition(conditionDto,pageable),HttpStatus.OK);
    }

    //슬랙 메시지 수정
    @Secured(Authority.MASTER)
    @PutMapping("/{slackMessageId}")
    public ResponseEntity<?> UpdateSlackMessageById(@RequestBody UpdateSlackMessageRequestDto updateSlackMessageRequestDto,@PathVariable UUID slackMessageId){

        SlackMessageDto slackMessageDto = mapperConfig.strictMapper().map(updateSlackMessageRequestDto, SlackMessageDto.class);
        slackMessageService.updateMessage(updateSlackMessageRequestDto,slackMessageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //슬랙 메시지 삭제
    @Secured(Authority.MASTER)
    @DeleteMapping("/{slackMessageId}")
    public ResponseEntity<?> deleteSlackMessageById(@PathVariable UUID slackMessageId){
        slackMessageService.deleteMessage(slackMessageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
