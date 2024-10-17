package com.coopang.product.application.service.product;

import com.coopang.apiconfig.error.AccessDeniedException;
import com.coopang.apidata.application.company.response.CompanyResponse;
import com.coopang.apidata.application.hub.response.HubResponse;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.product.application.service.feignclient.CompanyFeignClientService;
import com.coopang.product.application.service.feignclient.HubFeignClientService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductPermissionValidator {

    private final CompanyFeignClientService companyFeignClientService;
    private final HubFeignClientService hubFeignClientService;

    //업체 관리자 권한 확인
    public void verifyCompanyOfCompanyManager(String userRole, UUID companyId, UUID companyManagerId){
        if(UserRoleEnum.isCompany(userRole))
        {
            CompanyResponse companyResponse = companyFeignClientService.getCompanyById(companyId);

            if(!companyManagerId.equals(companyResponse.getCompanyManagerId()))
            {
                throw new AccessDeniedException("업체 관리자만 상품관리를 할 수 있습니다.");
            }
        }
    }

    //소속 허브 관리자 권한 확인 및 허브에 소속 업체인지 확인
    public void verifyCompanyOfHubManager(String userRole, UUID companyId, UUID hubManagerId){
        if(UserRoleEnum.isHubManager(userRole))
        {
            CompanyResponse companyResponse = companyFeignClientService.getCompanyById(companyId);

            UUID hubId = companyResponse.getHubId();

            HubResponse hubResponse = hubFeignClientService.getHubInfoById(hubId);

            if(!hubManagerId.equals(hubResponse.getHubManagerId()))
            {
                throw new AccessDeniedException("소속된 업체만 관리할 수 있습니다.");
            }
        }
    }
}
