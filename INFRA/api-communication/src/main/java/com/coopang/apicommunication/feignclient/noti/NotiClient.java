package com.coopang.apicommunication.feignclient.noti;

import com.coopang.apidata.application.noti.request.CreateSlackMessageRequest;
import com.coopang.apidata.application.noti.request.SlackMessageSearchConditionRequest;
import com.coopang.apidata.application.noti.response.SlackMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "ainoti", contextId = "notiClient", configuration = com.coopang.apiconfig.feignclient.FeignConfig.class)
public interface NotiClient {
    @PostMapping("/slack-messages/v1/slack-message")
    SlackMessageResponse createSlackMessage(CreateSlackMessageRequest req);

    @GetMapping("/slack-messages/v1/slack-message/{slackMessageId}")
    SlackMessageResponse getSlackMessageInfo(@PathVariable("slackMessageId") UUID slackMessageId);

    @PostMapping("/slack-messages/v1/slack-message/list")
    List<SlackMessageResponse> getSlackMessageList(@RequestBody SlackMessageSearchConditionRequest req);
}