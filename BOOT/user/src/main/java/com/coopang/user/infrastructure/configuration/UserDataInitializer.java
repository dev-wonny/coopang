package com.coopang.user.infrastructure.configuration;

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
import static com.coopang.apidata.constants.HubRegions.getHubRegions;
import static com.coopang.apidata.constants.UserConstants.COOPANG_EMAIL;
import static com.coopang.apidata.constants.UserConstants.COOPANG_LOWERCASE;

import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.user.application.request.UserDto;
import com.coopang.user.application.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "data.init.enabled", havingValue = "true", matchIfMissing = false)
public class UserDataInitializer implements CommandLineRunner {
    private final UserService userService;
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

    private int uuidIndex = 0;

    public UserDataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        createMasterUsers();
        createHubManagers();
        createShipperUsers();
        createCustomers();
        createCompanyUsers();
    }

    // 고정된 uuid
    String[] fixedUuids = {
            "3e864e21-15c8-47e1-b3b3-c669abf7f0fa",
            "40d78eb3-ca15-44ac-a5b5-182830615f4f",
            "e73eb204-8f6e-4c61-8a5f-5df1c0a2797e",
            "a96f086f-87cf-4c7d-99e8-1c31db14ca5e",
            "d55c8cdb-760d-4dd3-ac77-c30d2cbf787d",
            "4e1d9435-820a-432e-abe3-b7a11c0fbeea",
            "7a612c48-3c7c-4333-a54c-032400745cf6",
            "3a5ed5e5-8ee1-42c2-b201-9949a99f6b72",
            "0008e5b7-b5a6-41f2-8e3c-29156039a642",
            "249196ce-33df-4f86-a46d-9d42fead886b",
            "8fccf12a-13db-4e8e-9a2c-cbf85cb24a04",
            "ddab89d6-a024-43aa-b6a1-826110786686",
            "55de6337-556d-43c6-adbf-469c2cf2dea9",
            "e8c54e27-92cc-4780-a4d9-2ada1a577a34",
            "aa2a4b10-c91e-4993-8e28-a907602f13d2",
            "239f11df-4280-43b4-81a4-7139b31b071b",
            "3a1386db-8ad3-4cac-a234-31925ada139b",
            "ec397ca8-dfd0-4b94-912c-233265282316",
            "30ecd2e9-8dee-4478-b418-a7a293805772",
            "d8d5414e-7812-4b01-99e2-1055cbccf676",
            "0cfeddd6-e080-4a1f-b918-b80e0064cf8c",
            "bb659b6b-da76-4cd9-ae54-e3d5dc671910",
            "6b4f5a31-5345-45c7-a167-dd1fd2cd6ae8",
            "fdb75eed-390a-4aad-8bed-611829fba713",
            "5800e01b-5e60-4261-ade6-20953f7e9d24",
            "58e95489-8a76-4e46-84b5-ac7ce78e35ca",
            "48410e1f-69c2-41d6-bd6e-892329bff329",
            "ad1ee1d0-53fd-495f-9f35-2cdb2aa6be05",
            "6a26ed86-79f6-46c8-a830-239e9bd45350",
            "fd96ab07-3350-4298-8570-34ca189315ee",
            "afa5f951-02eb-4442-9fa1-363bc8dcec26",
            "0c433c56-4c5d-45f2-989a-8db15f55226a",
            "2e418446-0e43-4af9-b34a-beb1470a87cb",
            "9440bc94-26cb-4a7a-8668-371f19ace9dc",
            "bd1fa464-5b68-4d09-977c-015f3e9c2f6a",
            "b4c8a76c-df35-4066-8203-3df2455fcf88",
            "99f26f79-1ed5-4799-b7a0-058a00df55eb",
            "37d3078c-2a9a-485d-8a2f-09f5c6874e94",
            "06abaa95-2431-4ff5-8472-5bc17a16a5c3",
            "b320b1f4-197a-41de-bb68-b882fc0b2870",
            "531de8b3-2008-4f13-9511-7ee406c2e14a",
            "afa88bdc-0633-4c01-a220-5308ccb5e765",
            "0d8ca948-6881-4dd6-a4a9-79ebe2692d8d",
            "d2bcfe27-a27c-4efb-978a-22c5c674929c",
            "0f343a78-e7a9-47f1-a669-bb301acbc7db",
            "4b933781-8b93-4ebf-bfd1-4ca447cd9416",
            "1d954968-9cad-4526-a1df-5cce4aace3ae",
            "ef88eab0-66a3-43fd-894b-5e3d1bbfc348",
            "eb9169b3-b3d8-4f9b-8e31-95706711fd41",
            "bbb4b3d5-ccfe-467d-a7a5-b5e8a711230a",
            "cf004528-251f-4ced-89f1-2cb6d81c9b58",
            "53e4ad5c-0a53-446b-b31a-11416a4d364e",
            "bcdd0a57-cc4a-43ff-bb09-b6b9fb8c5d19",//Customer0
            "e1e04dd1-ce0a-4b96-85e0-540f1f72e585",//Customer1
            "ede7fea2-c4dc-4d8e-a5f2-70c010d80b93",
            "4541b728-7b2e-48e6-8bd3-b336303c74d2",
            "021d9eec-6bb7-496b-a869-9b27498ba188",
            "be322296-d5b1-42cb-9502-a8dc9c42db7f",
            "a6867398-dc56-4a8f-938c-581d597a7916",
            "4d7aff3d-a455-43d8-8675-eeaab76eb5d3",
            "c14b44a1-28f3-4c58-8700-b616ff141fc6",//Customer8
            "ac9bcee5-709c-4f10-8b2c-d45418aed6de",
            "a698a623-aa8f-4137-9951-a407af4025ab",
            "8233f07c-f3eb-44a0-9522-563304369768",//Customer-충북
            "ec75ee25-1ee5-404f-a156-ddeba231daae",//CompanyManager 1 - 서울
            "5e69568e-9a30-442b-bcd0-a8a30a80ac0e",//CompanyManager 2 - 서울
            "1f19e2ff-d270-4d55-bf82-91739bf344ab",
            "01b5bca3-aedb-44b3-9d1a-f5fdbaca141d",//CompanyManager 2 - 경기북부
            "6619735d-66f2-4388-a17e-f1d6074669b3",
            "d1f7b37c-96c1-48d8-a6c8-2c6dab9545ec",
            "58f8dd25-2383-4edf-a0a7-1a85ac312bc4",//부산
            "2b236f99-4a5f-4542-97c3-341f0d9ebc57",
            "7cc32daf-cd16-444c-a6a9-e5ca8923c4f5",//CompanyManager 1 - 대구
            "56d419ff-20cc-4469-9e17-9ae9f0735dde",
            "dc578d2e-c850-471a-86b7-c1b760c9f35b",//인천
            "10b830f1-aa78-433d-9d34-e46dc450b1ca",
            "16ba1a03-fd9e-429b-a047-558f8c67bbe8",//광주
            "7a124a90-d15e-4e9d-b1b8-1e31e43524eb",
            "0ea3bd2a-d202-4f9e-bade-d7f2defbf03e",//대전
            "735d2c64-5c0a-41ea-bf71-53e9f49b9b09",
            "b6bdb9bc-a2e7-4da6-bc96-417a990e5cd0",//울산
            "9f049e8c-d207-4dce-9fcd-dbae49432b81",
            "372e5bb4-4d93-42a0-bf50-3f99094c35c6",//세종
            "aafba163-0224-45fe-9005-2ac36a1e5847",
            "d44242a9-74db-4c01-8b10-8617ab1cb1ad",//강원
            "a5295c52-3dfa-467f-aafb-3f728ba2a4e5",
            "ef7a4240-c305-42fd-a5ee-42cbb99cdbaf",//CompanyManager 1 - 충북
            "17c1ba04-e7db-4f78-a8df-cfbd02582d0c"//CompanyManager 2 - 충북
//            "814332a7-54ba-4640-bc65-6cec63c5b96b",
//            "d5f4e84b-84c8-43d2-b2b7-27d883d7b17d",
//            "48b50467-1bb4-42d0-a653-e753239210a3",
//            "a7104e8e-9834-4486-94f3-c0b2db10277d",
//            "746f8699-5753-4458-91c6-37937153cf8a",
//            "57f7d9fd-1b80-4647-bb07-6fc454620647",
//            "1827468a-5522-417f-9979-34ecae003e10",
//            "4a89c46b-eaf3-49db-a0ad-862f87dc7fab",
//            "38f078b2-d4d4-404f-a4b8-d3f5b360e63b",
//            "15ea2d34-1d6b-4641-a014-7759e1e0062a",
//            "3bccf246-92f1-425b-9c9a-904c5458ea1d",
//            "118acc6b-954b-4df7-b1d8-2ecb10e62bf7",
//            "37f487ec-675c-48de-a653-cb0c1485adc6",
//            "2f86ccac-7800-4182-b6ec-9c489d515bfe",
//            "2c1cafc4-4085-4ebd-9b0e-872a5b6e2031",
//            "e9035f9e-924f-45e4-a461-b8e7e0ab651e",
//            "fac08a5f-1039-46bb-bc62-7487e0042aa5",
//            "903d74e4-7c0d-436a-b259-9ebe1b9c2fb7",
//            "739586df-8dac-443a-be5a-cf4a10863c4a",
//            "538df806-4e21-4eec-8e65-8301c3c508d3",
//            "82cf3fdc-ab02-40ca-960f-bd6df52c85f4",
//            "333698b2-26a0-4a39-90d5-83d3d881f0ed",
//            "62c5850e-005f-47ae-8f0f-559df09fe681"
    };

    // 고정된 UUID 배열에서 하나를 가져오는 메소드
    private UUID getNextFixedUuid() {
        if (uuidIndex >= fixedUuids.length) {
            throw new IllegalStateException("고정된 UUID가 부족합니다.");
        }
        return UUID.fromString(fixedUuids[uuidIndex++]);
    }

    // 공통 메서드: User 생성
    private void createUser(UserDto userDto) {
        userService.join(userDto);
    }

    // Master 4명 생성
    private void createMasterUsers() {
        for (int i = 1; i <= 4; i++) {
            UserDto masterDto = new UserDto();
            masterDto.setUserId(getNextFixedUuid());
            masterDto.setEmail("master" + i + COOPANG_EMAIL);
            masterDto.setPassword(COOPANG_LOWERCASE);
            masterDto.setUserName("Master" + i);
            masterDto.setPhoneNumber("010-1111-1111");
            masterDto.setRole(UserRoleEnum.MASTER.name());
            masterDto.setSlackId("master" + i);
            masterDto.setZipCode("11111");
            masterDto.setAddress1("서울특별시 송파구 송파대로 570");
            masterDto.setAddress2("101동");
            masterDto.setNearHubId(hubMap.get(SEOUL));

            createUser(masterDto);
        }
    }

    // Hub Manager 11명 생성
    private void createHubManagers() {
        String[] hubRegions = getHubRegions();
        for (int i = 0; i < hubRegions.length; i++) {
            UserDto hubManagerDto = new UserDto();
            hubManagerDto.setUserId(getNextFixedUuid());
            hubManagerDto.setEmail("HubManager" + (i + 1) + COOPANG_EMAIL);
            hubManagerDto.setPassword(COOPANG_LOWERCASE);
            hubManagerDto.setUserName("HubManager" + (i + 1));
            hubManagerDto.setPhoneNumber("010-1111-1111");
            hubManagerDto.setRole(UserRoleEnum.HUB_MANAGER.name());
            hubManagerDto.setSlackId("HubManager" + (i + 1));
            hubManagerDto.setZipCode("11111");
            hubManagerDto.setAddress1(hubRegions[i]);
            hubManagerDto.setAddress2("102동");
            hubManagerDto.setNearHubId(hubMap.get(hubRegions[i]));

            createUser(hubManagerDto);
        }
    }

    // Customer 각 허브별로 1명씩 생성
    private void createCustomers() {
        String[] hubRegions = getHubRegions();
        for (int i = 0; i < hubRegions.length; i++) {
            String hubName = hubRegions[i].replaceAll("\\s", "_").toLowerCase();

            UserDto customerDto = new UserDto();
            customerDto.setUserId(getNextFixedUuid());
            customerDto.setEmail("customer_" + hubName + COOPANG_EMAIL);
            customerDto.setPassword(COOPANG_LOWERCASE);
            customerDto.setUserName("Customer-" + hubRegions[i]);
            customerDto.setPhoneNumber("010-1111-1111");
            customerDto.setRole(UserRoleEnum.CUSTOMER.name());
            customerDto.setSlackId("Customer" + i);
            customerDto.setZipCode("11111");
            customerDto.setAddress1(hubName);
            customerDto.setAddress2("103동");
            customerDto.setNearHubId(hubMap.get(hubRegions[i]));

            createUser(customerDto);
        }
    }

    // Shipper 사용자 33명 생성
    private void createShipperUsers() {
        String[] hubRegions = getHubRegions();

        for (int i = 0; i < hubRegions.length; i++) {
            String hubName = hubRegions[i].replaceAll("\\s", "_").toLowerCase();

            // Shipper Hub 생성
            UserDto shipperHubDto = new UserDto();
            shipperHubDto.setUserId(getNextFixedUuid());
            shipperHubDto.setEmail("shipperHub_" + hubName + COOPANG_EMAIL);
            shipperHubDto.setPassword(COOPANG_LOWERCASE);
            shipperHubDto.setUserName("ShipperHub-" + hubRegions[i]);
            shipperHubDto.setPhoneNumber("010-1111-1111");
            shipperHubDto.setRole(UserRoleEnum.SHIPPER.name());
            shipperHubDto.setSlackId("ShipperHub" + i);
            shipperHubDto.setZipCode("11111");
            shipperHubDto.setAddress1(hubName);
            shipperHubDto.setAddress2("104동");
            shipperHubDto.setNearHubId(hubMap.get(hubRegions[i]));

            createUser(shipperHubDto);

            // Shipper Customer 2명 생성
            for (int j = 1; j <= 2; j++) {
                UserDto shipperCustomerDto = new UserDto();
                shipperCustomerDto.setUserId(getNextFixedUuid());
                shipperCustomerDto.setEmail("shipperCustomer_" + j + "_" + hubName + COOPANG_EMAIL);
                shipperCustomerDto.setPassword(COOPANG_LOWERCASE);
                shipperCustomerDto.setUserName("ShipperCustomer " + j + " - " + hubRegions[i]);
                shipperCustomerDto.setPhoneNumber("010-1111-1111");
                shipperCustomerDto.setRole(UserRoleEnum.SHIPPER.name());
                shipperCustomerDto.setSlackId("ShipperCustomer" + i);
                shipperCustomerDto.setZipCode("11111");
                shipperCustomerDto.setAddress1(hubName);
                shipperCustomerDto.setAddress2("105동");
                shipperCustomerDto.setNearHubId(hubMap.get(hubRegions[i]));

                createUser(shipperCustomerDto);
            }
        }
    }

    // Company 사용자 생성 (허브별 2명씩)
    private void createCompanyUsers() {
        String[] hubRegions = getHubRegions();

        for (int i = 0; i < hubRegions.length; i++) {
            String hubName = hubRegions[i].replaceAll("\\s", "_").toLowerCase();

            for (int j = 1; j <= 2; j++) {
                UserDto companyDto = new UserDto();
                companyDto.setUserId(getNextFixedUuid());
                companyDto.setEmail("company" + j + "_" + hubName + COOPANG_EMAIL);
                companyDto.setPassword(COOPANG_LOWERCASE);
                companyDto.setUserName("CompanyManager " + j + " - " + hubRegions[i]);
                companyDto.setPhoneNumber("010-1111-1111");
                companyDto.setRole(UserRoleEnum.COMPANY.name());
                companyDto.setSlackId("CompanyManager" + j);
                companyDto.setZipCode("11111");
                companyDto.setAddress1(hubName);
                companyDto.setAddress2("106동");
                companyDto.setNearHubId(hubMap.get(hubRegions[i]));

                createUser(companyDto);
            }
        }
    }
}