package com.coopang.product.application.service.feignclient;

import com.coopang.apiconfig.error.AccessDeniedException;
import com.coopang.apidata.application.company.response.CompanyResponse;
import com.coopang.apidata.application.hub.response.HubResponse;
import com.coopang.coredata.user.enums.UserRoleEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubManagerPermissionValidator {

    private final CompanyFeignClientService companyFeignClientService;
    private final HubFeignClientService hubFeignClientService;

    //소속 허브 관리자 권한 확인 및 허브에 소속 업체인지 확인
    public void validateHubManager( UUID hubManagerId, UUID companyId){
        CompanyResponse companyResponse = companyFeignClientService.getCompanyById(companyId);
        UUID hubId = companyResponse.getHubId();
        HubResponse hubResponse = hubFeignClientService.getHubInfoById(hubId);

        if(!hubManagerId.equals(hubResponse.getHubManagerId()))
        {
            throw new AccessDeniedException("소속된 업체만 관리할 수 있습니다.");
        }
    }
}
