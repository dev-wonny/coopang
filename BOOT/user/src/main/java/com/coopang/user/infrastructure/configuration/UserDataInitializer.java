package com.coopang.user.infrastructure.configuration;

import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.user.application.request.UserDto;
import com.coopang.user.domain.entity.user.UserEntity;
import com.coopang.user.infrastructure.repository.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserDataInitializer implements CommandLineRunner {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private int uuidIndex = 0;

    public UserDataInitializer(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createMasterUsers();
        createHubManagers();
        createShipperUsers();
        createCustomers();
    }

    // 허브 지역 정보 제공
    private String[] getHubRegions() {
        return new String[] {
                "서울", "경기북부", "경기남부", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "강원", "충북"
        };
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
            "bcdd0a57-cc4a-43ff-bb09-b6b9fb8c5d19",
            "e1e04dd1-ce0a-4b96-85e0-540f1f72e585",
            "ede7fea2-c4dc-4d8e-a5f2-70c010d80b93",
            "4541b728-7b2e-48e6-8bd3-b336303c74d2",
            "021d9eec-6bb7-496b-a869-9b27498ba188",
            "be322296-d5b1-42cb-9502-a8dc9c42db7f",
            "a6867398-dc56-4a8f-938c-581d597a7916",
            "4d7aff3d-a455-43d8-8675-eeaab76eb5d3",
            "c14b44a1-28f3-4c58-8700-b616ff141fc6",
            "ac9bcee5-709c-4f10-8b2c-d45418aed6de",
            "a698a623-aa8f-4137-9951-a407af4025ab"
    };

    // 고정된 UUID 배열에서 하나를 가져오는 메소드
    private UUID getNextFixedUuid() {
        if (uuidIndex >= fixedUuids.length) {
            throw new IllegalStateException("고정된 UUID가 부족합니다.");
        }
        return UUID.fromString(fixedUuids[uuidIndex++]);
    }

    // 공통 메서드: User 생성
    private void createUser(UUID userId, UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        userJpaRepository.save(UserEntity.create(
                userId,
                userDto.getEmail(),
                encodedPassword,
                userDto.getUserName(),
                userDto.getPhoneNumber(),
                userDto.getRole(),
                userDto.getSlackId(),
                userDto.getZipCode(),
                userDto.getAddress1(),
                userDto.getAddress2(),
                userDto.getNearHubId()
        ));
    }

    // Master 4명 생성
    private void createMasterUsers() {
        for (int i = 1; i <= 4; i++) {
            UserDto masterDto = new UserDto();
            masterDto.setEmail("master" + i + "@coopang.com");
            masterDto.setPassword("coopang");
            masterDto.setUserName("Master" + i);
            masterDto.setPhoneNumber("010-1111-1111");
            masterDto.setRole(UserRoleEnum.MASTER.name());
            masterDto.setSlackId("master" + i);
            masterDto.setZipCode("11111");
            masterDto.setAddress1("서울특별시");
            masterDto.setAddress2("101동");

            createUser(getNextFixedUuid(), masterDto);
        }
    }

    // Hub Manager 11명 생성
    private void createHubManagers() {
        String[] hubRegions = getHubRegions();
        for (int i = 1; i <= 11; i++) {
            UserDto hubManagerDto = new UserDto();
            hubManagerDto.setEmail("hub_manager" + i + "@coopang.com");
            hubManagerDto.setPassword("coopang");
            hubManagerDto.setUserName("HubManager" + i);
            hubManagerDto.setPhoneNumber("010-1111-1111");
            hubManagerDto.setRole(UserRoleEnum.HUB_MANAGER.name());
            hubManagerDto.setSlackId("HubManager" + i);
            hubManagerDto.setZipCode("11111");
            hubManagerDto.setAddress1(hubRegions[i]);
            hubManagerDto.setAddress2("102동");
            createUser(getNextFixedUuid(), hubManagerDto);
        }
    }

    // Customer 각 허브별로 1명씩 생성
    private void createCustomers() {
        String[] hubRegions = getHubRegions();
        for (int i = 0; i < hubRegions.length; i++) {
            String hubName = hubRegions[i].replaceAll("\\s", "_").toLowerCase();

            UserDto customerDto = new UserDto();
            customerDto.setEmail("customer_" + hubName + "@coopang.com");
            customerDto.setPassword("coopang");
            customerDto.setUserName("Customer-" + hubRegions[i]);
            customerDto.setPhoneNumber("010-1111-1111");
            customerDto.setRole(UserRoleEnum.CUSTOMER.name());
            customerDto.setSlackId("Customer" + i);
            customerDto.setZipCode("11111");
            customerDto.setAddress1(hubName);
            customerDto.setAddress2("103동");

            createUser(getNextFixedUuid(), customerDto);
        }
    }

    // Shipper 사용자 33명 생성
    private void createShipperUsers() {
        String[] hubRegions = getHubRegions();

        for (int i = 0; i < hubRegions.length; i++) {
            String hubName = hubRegions[i].replaceAll("\\s", "_").toLowerCase();

            // Shipper Hub 생성
            UserDto shipperHubDto = new UserDto();
            shipperHubDto.setEmail("shipperHub_" + hubName + "@coopang.com");
            shipperHubDto.setPassword("coopang");
            shipperHubDto.setUserName("ShipperHub-" + hubRegions[i]);
            shipperHubDto.setPhoneNumber("010-1111-1111");
            shipperHubDto.setRole(UserRoleEnum.SHIPPER.name());
            shipperHubDto.setSlackId("ShipperHub" + i);
            shipperHubDto.setZipCode("11111");
            shipperHubDto.setAddress1(hubName);
            shipperHubDto.setAddress2("104동");

            createUser(getNextFixedUuid(), shipperHubDto);

            // Shipper Customer 2명 생성
            for (int j = 1; j <= 2; j++) {
                UserDto shipperCustomerDto = new UserDto();
                shipperCustomerDto.setEmail("shipperCustomer_" + j + "_" + hubName + "@coopang.com");
                shipperCustomerDto.setPassword("coopang");
                shipperCustomerDto.setUserName("ShipperCustomer " + j + " - " + hubRegions[i]);
                shipperCustomerDto.setPhoneNumber("010-1111-1111");
                shipperCustomerDto.setRole(UserRoleEnum.SHIPPER.name());
                shipperCustomerDto.setSlackId("ShipperCustomer" + i);
                shipperCustomerDto.setZipCode("11111");
                shipperCustomerDto.setAddress1(hubName);
                shipperCustomerDto.setAddress2("105동");

                createUser(getNextFixedUuid(), shipperCustomerDto);
            }
        }
    }
}