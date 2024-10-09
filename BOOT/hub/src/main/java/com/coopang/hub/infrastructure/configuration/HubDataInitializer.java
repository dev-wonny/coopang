package com.coopang.hub.infrastructure.configuration;

import com.coopang.hub.application.request.hub.HubDto;
import com.coopang.hub.application.service.hub.HubService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HubDataInitializer implements CommandLineRunner {
    private final HubService hubService;

    public HubDataInitializer(HubService hubService) {
        this.hubService = hubService;
    }

    @Override
    public void run(String... args) throws Exception {
        createHubs();
    }

    private void createHubs() {
        createHub("서울특별시 센터", "서울특별시 송파구 송파대로 55", "11111", "서울", UUID.fromString("d55c8cdb-760d-4dd3-ac77-c30d2cbf787d"), UUID.fromString("12fc1d66-4270-4468-b76d-93264856327b"));
        createHub("경기 북부 센터", "경기도 고양시 덕양구 권율대로 570", "11111", "경기북부", UUID.fromString("4e1d9435-820a-432e-abe3-b7a11c0fbeea"), UUID.fromString("4dd1bc35-fecf-40a6-a3cd-281a6a800752"));
        createHub("경기 남부 센터", "경기도 이천시 덕평로 257-21", "11111", "경기남부", UUID.fromString("7a612c48-3c7c-4333-a54c-032400745cf6"), UUID.fromString("83f3e5ab-adda-4d53-adb1-bf2e00f80af6"));
        createHub("부산광역시 센터", "부산 동구 중앙대로 206", "11111", "부산", UUID.fromString("3a5ed5e5-8ee1-42c2-b201-9949a99f6b72"), UUID.fromString("83004b5d-122a-4bab-a27c-78824da0ad77"));
        createHub("대구광역시 센터", "대구 북구 태평로 161", "11111", "대구", UUID.fromString("0008e5b7-b5a6-41f2-8e3c-29156039a642"), UUID.fromString("d25e0314-0b9d-47dc-8a35-afd4381d3a9e"));
        createHub("인천광역시 센터", "인천 남동구 정각로 29", "11111", "인천", UUID.fromString("249196ce-33df-4f86-a46d-9d42fead886b"), UUID.fromString("094f9450-8916-4d5d-872f-af929cba79f7"));
        createHub("광주광역시 센터", "광주 서구 내방로 111", "11111", "광주", UUID.fromString("8fccf12a-13db-4e8e-9a2c-cbf85cb24a04"), UUID.fromString("297e573f-a450-46c4-b0ac-3d318ae66e27"));
        createHub("대전광역시 센터", "대전 서구 둔산로 100", "11111", "대전", UUID.fromString("ddab89d6-a024-43aa-b6a1-826110786686"), UUID.fromString("f2bea779-3987-474b-a044-d707551ad689"));
        createHub("울산광역시 센터", "울산 남구 중앙로 201", "11111", "울산", UUID.fromString("55de6337-556d-43c6-adbf-469c2cf2dea9"), UUID.fromString("773db62c-eae9-4d4e-9029-d24f348f8c61"));
        createHub("세종특별자치시 센터", "세종특별자치시 한누리대로 2130", "11111", "세종", UUID.fromString("e8c54e27-92cc-4780-a4d9-2ada1a577a34"), UUID.fromString("c3aebe6d-a257-4798-b3ce-b9595c3c59f3"));
        createHub("강원특별자치도 센터", "강원특별자치도 춘천시 중앙로 1", "11111", "강원", UUID.fromString("aa2a4b10-c91e-4993-8e28-a907602f13d2"), UUID.fromString("666278fe-006f-4895-a094-bac433c2d669"));
        createHub("충청북도 센터", "충북 청주시 상당구 상당로 82", "11111", "충북", UUID.fromString("3e864e21-15c8-47e1-b3b3-c669abf7f0fa"), UUID.fromString("c5fb540b-63f0-464a-951c-6cd711986440"));
    }

    private void createHub(String hubName, String address1, String zipCode, String region, UUID hubManagerId, UUID hubId) {
        HubDto hubDto = new HubDto();
        hubDto.setHubId(hubId);
        hubDto.setHubName(hubName);
        hubDto.setHubManagerId(hubManagerId);
        hubDto.setZipCode(zipCode);
        hubDto.setAddress1(address1);
        hubDto.setAddress2(region);
        hubService.createHub(hubDto);
    }
}
