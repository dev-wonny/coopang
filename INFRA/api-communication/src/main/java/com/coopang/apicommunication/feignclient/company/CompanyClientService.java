package com.coopang.apicommunication.feignclient.company;

import com.coopang.apidata.application.company.request.CompanySearchConditionRequest;
import com.coopang.apidata.application.company.response.CompanyResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyClientService {
    private final CompanyClient companyClient;

    public CompanyClientService(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    public CompanyResponse getCompanyInfo(UUID companyId) {
        return companyClient.getCompanyInfo(companyId);
    }

    public List<CompanyResponse> getCompanyList(CompanySearchConditionRequest req) {
        return companyClient.getCompanyList(req);
    }
}