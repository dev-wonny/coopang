package com.coopang.apicommunication.feignclient.noti;

import com.coopang.apidata.application.noti.request.CreateSlackMessageRequest;
import com.coopang.apidata.application.noti.request.SlackMessageSearchConditionRequest;
import com.coopang.apidata.application.noti.response.SlackMessageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "NotiClientController API", description = "NotiClientController API")
@Slf4j(topic = "NotiClientController")
@RequestMapping("/feignClient/v1/noti")
public class NotiClientController {
    private final NotiClientService notiClientService;

    public NotiClientController(NotiClientService notiClientService) {
        this.notiClientService = notiClientService;
    }

    @PostMapping
    SlackMessageResponse createSlackMessage(CreateSlackMessageRequest req) {
        return notiClientService.createSlackMessage(req);
    }

    @GetMapping("/{slackMessageId}")
    SlackMessageResponse getSlackMessageInfo(@PathVariable("slackMessageId") UUID slackMessageId) {
        return notiClientService.getSlackMessageInfo(slackMessageId);

    }

    @PostMapping("/list")
    List<SlackMessageResponse> getSlackMessageList(@RequestBody SlackMessageSearchConditionRequest req) {
        return notiClientService.getSlackMessageList(req);
    }
}
