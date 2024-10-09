package com.coopang.hub.infrastructure.configuration;

import com.coopang.hub.application.request.company.CompanyDto;
import com.coopang.hub.application.service.company.CompanyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CompanyDataInitializer implements CommandLineRunner {
    private final CompanyService companyService;
    private int uuidIndex = 0;

    public CompanyDataInitializer(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public void run(String... args) throws Exception {
        createCompanies();
    }

    // 허브 지역 정보 제공
    private String[] getHubRegions() {
        return new String[] {
                "서울", "경기북부", "경기남부", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "강원", "충북"
        };
    }

    // 고정된 hub_id 배열
    UUID[] hubIds = {
            UUID.fromString("12fc1d66-4270-4468-b76d-93264856327b"),  // 서울
            UUID.fromString("4dd1bc35-fecf-40a6-a3cd-281a6a800752"),  // 경기북부
            UUID.fromString("83f3e5ab-adda-4d53-adb1-bf2e00f80af6"),  // 경기남부
            UUID.fromString("83004b5d-122a-4bab-a27c-78824da0ad77"),  // 부산
            UUID.fromString("d25e0314-0b9d-47dc-8a35-afd4381d3a9e"),  // 대구
            UUID.fromString("094f9450-8916-4d5d-872f-af929cba79f7"),  // 인천
            UUID.fromString("297e573f-a450-46c4-b0ac-3d318ae66e27"),  // 광주
            UUID.fromString("f2bea779-3987-474b-a044-d707551ad689"),  // 대전
            UUID.fromString("773db62c-eae9-4d4e-9029-d24f348f8c61"),  // 울산
            UUID.fromString("c3aebe6d-a257-4798-b3ce-b9595c3c59f3"),  // 세종
            UUID.fromString("666278fe-006f-4895-a094-bac433c2d669"),  // 강원
            UUID.fromString("c5fb540b-63f0-464a-951c-6cd711986440")   // 충북
    };

    // COMPANY 권한을 가진 user_id 매핑 (지역별)
    private Map<String, UUID[]> companyManagers = new HashMap<>() {{
        put("서울", new UUID[] {
                UUID.fromString("4a89c46b-eaf3-49db-a0ad-862f87dc7fab"),
                UUID.fromString("38f078b2-d4d4-404f-a4b8-d3f5b360e63b")
        });
        put("경기북부", new UUID[] {
                UUID.fromString("2f86ccac-7800-4182-b6ec-9c489d515bfe"),
                UUID.fromString("2c1cafc4-4085-4ebd-9b0e-872a5b6e2031")
        });
        put("경기남부", new UUID[] {
                UUID.fromString("a7104e8e-9834-4486-94f3-c0b2db10277d"),
                UUID.fromString("746f8699-5753-4458-91c6-37937153cf8a")
        });
        put("부산", new UUID[] {
                UUID.fromString("118acc6b-954b-4df7-b1d8-2ecb10e62bf7"),
                UUID.fromString("37f487ec-675c-48de-a653-cb0c1485adc6")
        });
        put("대구", new UUID[] {
                UUID.fromString("e9035f9e-924f-45e4-a461-b8e7e0ab651e"),
                UUID.fromString("fac08a5f-1039-46bb-bc62-7487e0042aa5")
        });
        put("인천", new UUID[] {
                UUID.fromString("903d74e4-7c0d-436a-b259-9ebe1b9c2fb7"),
                UUID.fromString("739586df-8dac-443a-be5a-cf4a10863c4a")
        });
        put("광주", new UUID[] {
                UUID.fromString("538df806-4e21-4eec-8e65-8301c3c508d3"),
                UUID.fromString("82cf3fdc-ab02-40ca-960f-bd6df52c85f4")
        });
        put("대전", new UUID[] {
                UUID.fromString("57f7d9fd-1b80-4647-bb07-6fc454620647"),
                UUID.fromString("1827468a-5522-417f-9979-34ecae003e10")
        });
        put("울산", new UUID[] {
                UUID.fromString("17c1ba04-e7db-4f78-a8df-cfbd02582d0c"),
                UUID.fromString("814332a7-54ba-4640-bc65-6cec63c5b96b")
        });
        put("세종", new UUID[] {
                UUID.fromString("15ea2d34-1d6b-4641-a014-7759e1e0062a"),
                UUID.fromString("3bccf246-92f1-425b-9c9a-904c5458ea1d")
        });
        put("강원", new UUID[] {
                UUID.fromString("333698b2-26a0-4a39-90d5-83d3d881f0ed"),
                UUID.fromString("62c5850e-005f-47ae-8f0f-559df09fe681")
        });
        put("충북", new UUID[] {
                UUID.fromString("d5f4e84b-84c8-43d2-b2b7-27d883d7b17d"),
                UUID.fromString("48b50467-1bb4-42d0-a653-e753239210a3")
        });
    }};


    // 허브별로 과일가게와 핸드폰 회사 생성
    private void createCompanies() {
        String[] hubRegions = getHubRegions();

        for (int i = 0; i < hubRegions.length; i++) {
            createFruitShopCompany(hubRegions[i], hubIds[i], companyManagers.get(hubRegions[i])[0]);
            createPhoneCompany(hubRegions[i], hubIds[i], companyManagers.get(hubRegions[i])[1]);
        }
    }

    // 과일가게 회사 생성
    private void createFruitShopCompany(String hubRegion, UUID hubId, UUID managerId) {
        CompanyDto fruitShopDto = new CompanyDto();
        fruitShopDto.setCompanyId(null);
        fruitShopDto.setCompanyName(hubRegion + " 과일가게");
        fruitShopDto.setHubId(hubId);
        fruitShopDto.setCompanyManagerId(managerId);  // 지역에 맞는 관리자 ID 설정
        fruitShopDto.setZipCode("11111");
        fruitShopDto.setAddress1(hubRegion + " 중심지");
        fruitShopDto.setAddress2("101동");

        companyService.createCompany(fruitShopDto);
    }

    // 핸드폰 회사 생성
    private void createPhoneCompany(String hubRegion, UUID hubId, UUID managerId) {
        CompanyDto phoneShopDto = new CompanyDto();
        phoneShopDto.setCompanyId(null);
        phoneShopDto.setCompanyName(hubRegion + " 핸드폰 가게");
        phoneShopDto.setHubId(hubId);
        phoneShopDto.setCompanyManagerId(managerId);  // 지역에 맞는 관리자 ID 설정
        phoneShopDto.setZipCode("11111");
        phoneShopDto.setAddress1(hubRegion + " 중심지");
        phoneShopDto.setAddress2("102동");

        companyService.createCompany(phoneShopDto);
    }
}
