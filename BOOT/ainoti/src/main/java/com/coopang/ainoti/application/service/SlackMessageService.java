package com.coopang.ainoti.application.service;

import com.coopang.ainoti.application.request.SlackMessageDto;
import com.coopang.ainoti.application.response.SlackMessageResponseDto;
import com.coopang.ainoti.domain.entitiy.slackMessage.SlackMessageEntity;
import com.coopang.ainoti.domain.repository.slackMessage.SlackMessageRepository;
import com.coopang.ainoti.domain.service.SlackMessageDomainService;
import com.coopang.ainoti.presentation.request.SlackMessageSearchConditionDto;
import com.coopang.ainoti.presentation.request.UpdateSlackMessageRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SlackMessageService {

    private final SlackMessageRepository slackMessageRepository;
    private final SlackMessageDomainService slackMessageDomainService;

    //슬랙 메시지 생성
    public SlackMessageResponseDto createMessage(SlackMessageDto dto) {

        return SlackMessageResponseDto.of(slackMessageDomainService.createMessage(dto));
    }

    //슬랙 메시지 논리적 삭제
    @Transactional
    public void deleteMessage(UUID slackMessageId) {

        SlackMessageEntity slackMessageEntity = findBySlackMessageId(slackMessageId);

        slackMessageEntity.setDeleted(true);
    }

    //슬랙 메시지 업데이트
    @Transactional
    public void updateMessage(UpdateSlackMessageRequestDto updateSlackMessageRequestDto, UUID slackMessageId) {
        SlackMessageEntity slackMessageEntity = findBySlackMessageId(slackMessageId);

        slackMessageEntity.update(
            updateSlackMessageRequestDto.getReceiveSlackId(),
            updateSlackMessageRequestDto.getReceiveUserId(),
            updateSlackMessageRequestDto.getSlackMessageStatus(),
            updateSlackMessageRequestDto.getSlackMessage(),
            updateSlackMessageRequestDto.getSlackMessageSenderId()
        );
    }

    //슬랙 메시지 조회하는 공통함수
    private SlackMessageEntity findBySlackMessageId(UUID slackMessageId) {
        return slackMessageRepository.findById(slackMessageId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 슬랙메시지 아이디입니다.")
        );
    }

    //슬렉 메시지 단일조회
    @Transactional(readOnly = true)
    public SlackMessageResponseDto getSlackMessage(UUID slackMessageId) {

        return SlackMessageResponseDto.of(findBySlackMessageId(slackMessageId));
    }

    //슬랙 메시지 목록 조회 - 페이징 지원
    public Page<SlackMessageResponseDto> getSlackMessagesWithPageable(Pageable pageable) {

        return slackMessageRepository.search(pageable).map(SlackMessageResponseDto::of);
    }

    //슬랙 메시지 목록 조회 - 페이징 지원,키워드 검색
    public Page<SlackMessageResponseDto> searchWithCondition(SlackMessageSearchConditionDto conditionDto, Pageable pageable) {
        return slackMessageRepository.searchWithCondition(conditionDto, pageable).map(SlackMessageResponseDto::of);
    }
}
