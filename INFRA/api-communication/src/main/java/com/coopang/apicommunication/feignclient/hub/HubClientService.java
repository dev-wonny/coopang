package com.coopang.apicommunication.feignclient.hub;

import com.coopang.apidata.application.hub.request.HubSearchConditionRequest;
import com.coopang.apidata.application.hub.response.HubResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Service
public class HubClientService {
    private final HubClient hubClient;

    public HubClientService(HubClient hubClient) {
        this.hubClient = hubClient;
    }

    public HubResponse getHubInfo(UUID hubId) {
        return hubClient.getHubInfo(hubId);
    }

    public List<HubResponse> getHubList(HubSearchConditionRequest req) {
        return hubClient.getHubList(req);
    }
}