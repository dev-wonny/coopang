package com.coopang.ainoti.application.scheduler;

import com.coopang.ainoti.application.service.noti.SlackMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlackMessageScheduler {

    private final SlackMessageService slackMessageService;

    public SlackMessageScheduler(SlackMessageService slackMessageService) {
        this.slackMessageService = slackMessageService;
    }

    /**
     * 매 1시간마다 상태가 READY인 메시지 중, 현재 시간이 전송 시간인 메시지를 전송하는 작업
     */
    @Scheduled(fixedRate = 3600000) // 1시간 간격
    public void sendReadySlackMessages() {
        log.info("스케쥴러 실행: 상태가 READY인 Slack 메시지들을 전송합니다.");
        slackMessageService.sendReadySlackMessages();
    }
}