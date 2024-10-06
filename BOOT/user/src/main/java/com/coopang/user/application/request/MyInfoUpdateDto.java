package com.coopang.user.application.request;

import lombok.Data;

@Data
public class MyInfoUpdateDto {
    private String userName;
    private String phoneNumber;
    private String slackId;
}