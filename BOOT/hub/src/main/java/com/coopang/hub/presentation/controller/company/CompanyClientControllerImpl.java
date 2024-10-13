package com.coopang.hub.presentation.controller.company;

import com.coopang.apicommunication.feignclient.company.CompanyClientController;
import com.coopang.apicommunication.feignclient.company.CompanyClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyClientControllerImpl extends CompanyClientController {
    protected CompanyClientControllerImpl(CompanyClientService companyClientService) {
        super(companyClientService);
    }
}
