package com.coopang.product.application.service.feignclient;

import com.coopang.apicommunication.feignclient.company.CompanyClientService;
import com.coopang.apiconfig.feignClient.FeignConfig;
import com.coopang.apidata.application.company.request.CompanySearchConditionRequest;
import com.coopang.apidata.application.company.response.CompanyResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyFeignClientService {

    private final CompanyClientService companyClientService;
    private final FeignConfig feignConfig;
    
    //내부통신을 이용하여 허브에 소속된 업체들을 조회 
    public List<UUID> getCompanyIdsInFeignClient(UUID hubId) {

        try{
            CompanySearchConditionRequest req = new CompanySearchConditionRequest();
            req.setHubId(hubId);
            req.setIsDeleted(false);
            //역할을 SERVER로 변경
            feignConfig.changeHeaderRoleToServer();
            //허브에 소속된 업체들 조회
            List<CompanyResponse> companyLists = companyClientService.getCompanyList(req);
            //업체에 포함된 업체들의 ID 추출
            List<UUID> companyIds = companyLists.stream()
                .map(CompanyResponse::getCompanyId)
                .toList();

            return companyIds;
        } finally {
            // 역할을 초기화
            feignConfig.resetRole();
        }
    }

    //업체의 관리자 조회 - 내부통신이용
    public CompanyResponse getCompanyById(UUID companyId) {

        try{

            feignConfig.changeHeaderRoleToServer();

            CompanyResponse companyResponse = companyClientService.getCompanyInfo(companyId);

            return companyResponse;
        } finally {
            // 역할을 초기화
            feignConfig.resetRole();
        }
    }
}
