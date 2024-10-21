package com.coopang.hub.infrastructure.configuration;

import static com.coopang.apidata.constants.HubRegions.BUSAN;
import static com.coopang.apidata.constants.HubRegions.CHUNGBUK;
import static com.coopang.apidata.constants.HubRegions.DAEGU;
import static com.coopang.apidata.constants.HubRegions.DAEJEON;
import static com.coopang.apidata.constants.HubRegions.GANGWON;
import static com.coopang.apidata.constants.HubRegions.GWANGJU;
import static com.coopang.apidata.constants.HubRegions.GYEONGGI_NORTH;
import static com.coopang.apidata.constants.HubRegions.GYEONGGI_SOUTH;
import static com.coopang.apidata.constants.HubRegions.INCHEON;
import static com.coopang.apidata.constants.HubRegions.SEJONG;
import static com.coopang.apidata.constants.HubRegions.SEOUL;
import static com.coopang.apidata.constants.HubRegions.ULSAN;

import com.coopang.hub.application.request.company.CompanyDto;
import com.coopang.hub.application.service.company.CompanyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "data.init.enabled", havingValue = "true", matchIfMissing = false)
public class CompanyDataInitializer implements CommandLineRunner {
    private final CompanyService companyService;

    private Map<String, UUID> hubMap = new HashMap<>();

    {
        hubMap.put(SEOUL, UUID.fromString("12fc1d66-4270-4468-b76d-93264856327b"));
        hubMap.put(GYEONGGI_NORTH, UUID.fromString("4dd1bc35-fecf-40a6-a3cd-281a6a800752"));
        hubMap.put(GYEONGGI_SOUTH, UUID.fromString("83f3e5ab-adda-4d53-adb1-bf2e00f80af6"));
        hubMap.put(BUSAN, UUID.fromString("83004b5d-122a-4bab-a27c-78824da0ad77"));
        hubMap.put(DAEGU, UUID.fromString("d25e0314-0b9d-47dc-8a35-afd4381d3a9e"));
        hubMap.put(INCHEON, UUID.fromString("094f9450-8916-4d5d-872f-af929cba79f7"));
        hubMap.put(GWANGJU, UUID.fromString("297e573f-a450-46c4-b0ac-3d318ae66e27"));
        hubMap.put(DAEJEON, UUID.fromString("f2bea779-3987-474b-a044-d707551ad689"));
        hubMap.put(ULSAN, UUID.fromString("773db62c-eae9-4d4e-9029-d24f348f8c61"));
        hubMap.put(SEJONG, UUID.fromString("c3aebe6d-a257-4798-b3ce-b9595c3c59f3"));
        hubMap.put(GANGWON, UUID.fromString("666278fe-006f-4895-a094-bac433c2d669"));
        hubMap.put(CHUNGBUK, UUID.fromString("c5fb540b-63f0-464a-951c-6cd711986440"));
    }

    private Map<String, UUID[]> companyManagers;

    public CompanyDataInitializer(CompanyService companyService) {
        this.setCompanyManagers();
        this.companyService = companyService;
    }

    @Override
    public void run(String... args) throws Exception {
        createCompanies();
    }


    // COMPANY 권한을 가진 user_id 매핑 (지역별)
    private void setCompanyManagers() {
        companyManagers = new HashMap<>();
        companyManagers.put(SEOUL, new UUID[] {
            UUID.fromString("ec75ee25-1ee5-404f-a156-ddeba231daae"),
            UUID.fromString("5e69568e-9a30-442b-bcd0-a8a30a80ac0e")
        });
        companyManagers.put(GYEONGGI_NORTH, new UUID[] {
            UUID.fromString("1f19e2ff-d270-4d55-bf82-91739bf344ab"),
            UUID.fromString("01b5bca3-aedb-44b3-9d1a-f5fdbaca141d")
        });
        companyManagers.put(GYEONGGI_SOUTH, new UUID[] {
            UUID.fromString("6619735d-66f2-4388-a17e-f1d6074669b3"),
            UUID.fromString("d1f7b37c-96c1-48d8-a6c8-2c6dab9545ec")
        });
        companyManagers.put(BUSAN, new UUID[] {
            UUID.fromString("58f8dd25-2383-4edf-a0a7-1a85ac312bc4"),
            UUID.fromString("2b236f99-4a5f-4542-97c3-341f0d9ebc57")
        });
        companyManagers.put(DAEGU, new UUID[] {
            UUID.fromString("7cc32daf-cd16-444c-a6a9-e5ca8923c4f5"),
            UUID.fromString("56d419ff-20cc-4469-9e17-9ae9f0735dde")
        });
        companyManagers.put(INCHEON, new UUID[] {
            UUID.fromString("dc578d2e-c850-471a-86b7-c1b760c9f35b"),
            UUID.fromString("10b830f1-aa78-433d-9d34-e46dc450b1ca")
        });
        companyManagers.put(GWANGJU, new UUID[] {
            UUID.fromString("16ba1a03-fd9e-429b-a047-558f8c67bbe8"),
            UUID.fromString("7a124a90-d15e-4e9d-b1b8-1e31e43524eb")
        });
        companyManagers.put(DAEJEON, new UUID[] {
            UUID.fromString("0ea3bd2a-d202-4f9e-bade-d7f2defbf03e"),
            UUID.fromString("735d2c64-5c0a-41ea-bf71-53e9f49b9b09")
        });
        companyManagers.put(ULSAN, new UUID[] {
            UUID.fromString("b6bdb9bc-a2e7-4da6-bc96-417a990e5cd0"),
            UUID.fromString("9f049e8c-d207-4dce-9fcd-dbae49432b81")
        });
        companyManagers.put(SEJONG, new UUID[] {
            UUID.fromString("372e5bb4-4d93-42a0-bf50-3f99094c35c6"),
            UUID.fromString("aafba163-0224-45fe-9005-2ac36a1e5847")
        });
        companyManagers.put(GANGWON, new UUID[] {
            UUID.fromString("d44242a9-74db-4c01-8b10-8617ab1cb1ad"),
            UUID.fromString("a5295c52-3dfa-467f-aafb-3f728ba2a4e5")
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
        final CompanyDto fruitShopDto = CompanyDto.of(
            null
            , hubId
            , managerId
            , hubRegion + " 과일가게"
            , "11111"
            , hubRegion + " 중심지"
            , "101동"
        );

        companyService.createCompany(fruitShopDto);
    }

    // 핸드폰 회사 생성
    private void createPhoneCompany(String hubRegion, UUID hubId, UUID managerId) {
        final CompanyDto phoneShopDto = CompanyDto.of(
            null
            , hubId
            , managerId
            , hubRegion + " 핸드폰 가게"
            , "11111"
            , hubRegion + " 중심지"
            , "102동"
        );
        companyService.createCompany(phoneShopDto);
    }
}