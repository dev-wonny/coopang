package com.coopang.apicommunication.feignclient.company;

import com.coopang.apidata.application.company.request.CompanySearchConditionRequest;
import com.coopang.apidata.application.company.response.CompanyResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "CompanyClientController API", description = "CompanyClientController API")
@Slf4j(topic = "CompanyClientController")
@RestController
@RequestMapping("/feignClient/v1/company")
public abstract class CompanyClientController {
    private final CompanyClientService companyClientService;

    protected CompanyClientController(CompanyClientService companyClientService) {
        this.companyClientService = companyClientService;
    }

    @GetMapping("/{companyId}")
    public CompanyResponse getCompanyInfo(@PathVariable("companyId") UUID companyId) {
        return companyClientService.getCompanyInfo(companyId);
    }

    @PostMapping("/list")
    public List<CompanyResponse> getCompanyList(@RequestBody CompanySearchConditionRequest req) {
        return companyClientService.getCompanyList(req);
    }
}
