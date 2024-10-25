package com.coopang.product.application.service.feignclient;

import com.coopang.apicommunication.feignclient.company.CompanyClientService;
import com.coopang.apiconfig.error.AccessDeniedException;
import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.company.request.CompanySearchConditionRequest;
import com.coopang.apidata.application.company.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyFeignClientService {

    private final CompanyClientService companyClientService;
    private final FeignConfig feignConfig;

    //내부통신을 이용하여 허브에 소속된 업체들을 조회 
    public List<UUID> getCompanyIdsInFeignClient(UUID hubId) {
        try {
            CompanySearchConditionRequest req = new CompanySearchConditionRequest();
            req.setHubId(hubId);
            req.setIsDeleted(false);

            //역할을 SERVER로 변경
            feignConfig.changeHeaderRoleToServer();
            //허브에 소속된 업체들 조회
            final List<CompanyResponse> companyList = companyClientService.getCompanyList(req);
            final List<UUID> companyIdList = companyList.stream()
                .map(CompanyResponse::getCompanyId)
                .toList();
            log.debug("getCompanyIdsInFeignClient:: companyIdList: {}", companyIdList);
            return companyIdList;
        } finally {
            // 역할을 초기화
            feignConfig.resetRole();
        }
    }

    //업체의 관리자 조회 - 내부통신이용
    public CompanyResponse getCompanyById(UUID companyId) {
        try {
            feignConfig.changeHeaderRoleToServer();
            final CompanyResponse companyResponse = companyClientService.getCompanyInfo(companyId);
            return companyResponse;
        } finally {
            // 역할을 초기화
            feignConfig.resetRole();
        }
    }

    public void validateCompanyOwner(UUID companyManagerId, UUID companyId) {
        final CompanyResponse companyResponse = companyClientService.getCompanyInfo(companyId);

        if (!companyManagerId.equals(companyResponse.getCompanyManagerId())) {
            throw new AccessDeniedException("업체 관리자만 상품관리를 할 수 있습니다.");
        }
    }
}
