package com.coopang.apicommunication.feignclient.company;

import com.coopang.apiconfig.feignclient.FeignConfig;
import com.coopang.apidata.application.company.request.CompanySearchConditionRequest;
import com.coopang.apidata.application.company.response.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub", contextId = "companyClient", configuration = FeignConfig.class)
public interface CompanyClient {
    @GetMapping("/companies/v1/company/{companyId}")
    CompanyResponse getCompanyInfo(@PathVariable("companyId") UUID companyId);

    @PostMapping("/companies/v1/company/list")
    List<CompanyResponse> getCompanyList(@RequestBody CompanySearchConditionRequest req);
}