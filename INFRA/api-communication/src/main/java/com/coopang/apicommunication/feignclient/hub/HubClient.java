package com.coopang.apicommunication.feignclient.hub;

import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.hub.request.HubSearchConditionRequest;
import com.coopang.apidata.application.hub.response.HubResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub", contextId = "hubClient", configuration = FeignConfig.class)
public interface HubClient {
    @GetMapping("/hubs/v1/hub/{hubId}")
    HubResponse getHubInfo(@PathVariable("hubId") UUID hubId);

    @PostMapping("/hubs/v1/hub/list")
    List<HubResponse> getHubList(@RequestBody HubSearchConditionRequest req);
}
