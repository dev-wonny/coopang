package com.coopang.hub.presentation.request.company;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanySearchConditionRequestDto {
    private UUID companyId;
    private UUID hubId;
    private UUID companyManagerId;
    private String companyName;//starsWith

    private String hubName;//starsWith
    private Boolean isDeleted;
}
