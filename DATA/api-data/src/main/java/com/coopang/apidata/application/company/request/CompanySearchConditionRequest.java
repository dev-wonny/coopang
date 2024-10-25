package com.coopang.apidata.application.company.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchConditionRequest {
    private UUID companyId;
    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;//starsWith

    private String hubName;//starsWith
    private Boolean isDeleted;
}
