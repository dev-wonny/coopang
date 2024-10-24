package com.coopang.product.application.service.feignclient;

import com.coopang.apicommunication.feignclient.hub.HubClientService;
import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.hub.response.HubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubFeignClientService {
    private final HubClientService hubClientService;
    private final FeignConfig feignConfig;

    public HubResponse getHubInfoById(UUID hubId) {
        try {
            feignConfig.changeHeaderRoleToServer();
            final HubResponse hubResponse = hubClientService.getHubInfo(hubId);
            return hubResponse;
        } finally {
            feignConfig.resetRole();
        }
    }
}
