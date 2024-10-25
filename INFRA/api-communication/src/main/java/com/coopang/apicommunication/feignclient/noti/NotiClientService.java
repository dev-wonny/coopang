package com.coopang.apicommunication.feignclient.noti;

import com.coopang.apidata.application.noti.request.CreateSlackMessageRequest;
import com.coopang.apidata.application.noti.request.SlackMessageSearchConditionRequest;
import com.coopang.apidata.application.noti.response.SlackMessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotiClientService {
    private final NotiClient notiClient;

    public NotiClientService(NotiClient notiClient) {
        this.notiClient = notiClient;
    }

    public SlackMessageResponse createSlackMessage(CreateSlackMessageRequest req) {
        return notiClient.createSlackMessage(req);
    }

    public SlackMessageResponse getSlackMessageInfo(UUID slackMessageId) {
        return notiClient.getSlackMessageInfo(slackMessageId);
    }

    public List<SlackMessageResponse> getSlackMessageList(SlackMessageSearchConditionRequest req) {
        return notiClient.getSlackMessageList(req);
    }
}
