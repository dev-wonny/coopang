package com.coopang.hub.infrastructure.configuration;

import static com.coopang.apiconfig.initdata.HubRegions.BUSAN;
import static com.coopang.apiconfig.initdata.HubRegions.CHUNGBUK;
import static com.coopang.apiconfig.initdata.HubRegions.DAEGU;
import static com.coopang.apiconfig.initdata.HubRegions.DAEJEON;
import static com.coopang.apiconfig.initdata.HubRegions.GANGWON;
import static com.coopang.apiconfig.initdata.HubRegions.GWANGJU;
import static com.coopang.apiconfig.initdata.HubRegions.GYEONGGI_NORTH;
import static com.coopang.apiconfig.initdata.HubRegions.GYEONGGI_SOUTH;
import static com.coopang.apiconfig.initdata.HubRegions.INCHEON;
import static com.coopang.apiconfig.initdata.HubRegions.SEJONG;
import static com.coopang.apiconfig.initdata.HubRegions.SEOUL;
import static com.coopang.apiconfig.initdata.HubRegions.ULSAN;

import com.coopang.apiconfig.initdata.HubMapInitializer;
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
    private Map<String, UUID> hubMap;
    private Map<String, UUID[]> companyManagers;

    public CompanyDataInitializer(CompanyService companyService, HubMapInitializer hubMapInitializer) {
        this.companyService = companyService;
        this.hubMap = hubMapInitializer.getHubMap();
        setCompanyManagers();
    }

    @Override
    public void run(String... args) throws Exception {
        createCompanies();
    }

    // COMPANY 권한을 가진 user_id 매핑 (지역별)
    private void setCompanyManagers() {
        companyManagers = new HashMap<>();
        companyManagers.put(SEOUL, new UUID[] {
                UUID.fromString("4a89c46b-eaf3-49db-a0ad-862f87dc7fab"),
                UUID.fromString("38f078b2-d4d4-404f-a4b8-d3f5b360e63b")
        });
        companyManagers.put(GYEONGGI_NORTH, new UUID[] {
                UUID.fromString("2f86ccac-7800-4182-b6ec-9c489d515bfe"),
                UUID.fromString("2c1cafc4-4085-4ebd-9b0e-872a5b6e2031")
        });
        companyManagers.put(GYEONGGI_SOUTH, new UUID[] {
                UUID.fromString("a7104e8e-9834-4486-94f3-c0b2db10277d"),
                UUID.fromString("746f8699-5753-4458-91c6-37937153cf8a")
        });
        companyManagers.put(BUSAN, new UUID[] {
                UUID.fromString("118acc6b-954b-4df7-b1d8-2ecb10e62bf7"),
                UUID.fromString("37f487ec-675c-48de-a653-cb0c1485adc6")
        });
        companyManagers.put(DAEGU, new UUID[] {
                UUID.fromString("e9035f9e-924f-45e4-a461-b8e7e0ab651e"),
                UUID.fromString("fac08a5f-1039-46bb-bc62-7487e0042aa5")
        });
        companyManagers.put(INCHEON, new UUID[] {
                UUID.fromString("903d74e4-7c0d-436a-b259-9ebe1b9c2fb7"),
                UUID.fromString("739586df-8dac-443a-be5a-cf4a10863c4a")
        });
        companyManagers.put(GWANGJU, new UUID[] {
                UUID.fromString("538df806-4e21-4eec-8e65-8301c3c508d3"),
                UUID.fromString("82cf3fdc-ab02-40ca-960f-bd6df52c85f4")
        });
        companyManagers.put(DAEJEON, new UUID[] {
                UUID.fromString("57f7d9fd-1b80-4647-bb07-6fc454620647"),
                UUID.fromString("1827468a-5522-417f-9979-34ecae003e10")
        });
        companyManagers.put(ULSAN, new UUID[] {
                UUID.fromString("17c1ba04-e7db-4f78-a8df-cfbd02582d0c"),
                UUID.fromString("814332a7-54ba-4640-bc65-6cec63c5b96b")
        });
        companyManagers.put(SEJONG, new UUID[] {
                UUID.fromString("15ea2d34-1d6b-4641-a014-7759e1e0062a"),
                UUID.fromString("3bccf246-92f1-425b-9c9a-904c5458ea1d")
        });
        companyManagers.put(GANGWON, new UUID[] {
                UUID.fromString("333698b2-26a0-4a39-90d5-83d3d881f0ed"),
                UUID.fromString("62c5850e-005f-47ae-8f0f-559df09fe681")
        });
        companyManagers.put(CHUNGBUK, new UUID[] {
                UUID.fromString("d5f4e84b-84c8-43d2-b2b7-27d883d7b17d"),
                UUID.fromString("48b50467-1bb4-42d0-a653-e753239210a3")
        });
    }

    // 허브별로 과일가게와 핸드폰 회사 생성
    private void createCompanies() {
        for (String region : hubMap.keySet()) {
            createFruitShopCompany(region, hubMap.get(region), companyManagers.get(region)[0]);
            createPhoneCompany(region, hubMap.get(region), companyManagers.get(region)[1]);
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