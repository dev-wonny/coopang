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

import com.coopang.apidata.application.hub.enums.ShipperTypeEnum;
import com.coopang.apidata.initdata.HubMapInitializer;
import com.coopang.hub.application.request.shipper.ShipperDto;
import com.coopang.hub.application.service.shipper.ShipperService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "data.init.enabled", havingValue = "true", matchIfMissing = false)
public class ShipperDataInitializer implements CommandLineRunner {
    private final ShipperService shipperService;
    private Map<String, UUID> hubMap;

    public ShipperDataInitializer(ShipperService shipperService) {
        this.shipperService = shipperService;
        HubMapInitializer hubMapInitializer = new HubMapInitializer();
        this.hubMap = hubMapInitializer.getHubMap();
    }

    @Override
    public void run(String... args) throws Exception {
        createShippers();
    }

    // hubId에 매핑되는 지역 데이터
    List<ShipperDto> shipperDtoList = List.of(
            // 서울
            createShipperDto("3a1386db-8ad3-4cac-a234-31925ada139b", SEOUL, "ShipperHub-서울"),
            createShipperDto("ec397ca8-dfd0-4b94-912c-233265282316", SEOUL, "ShipperCustomer 1 - 서울"),
            createShipperDto("30ecd2e9-8dee-4478-b418-a7a293805772", SEOUL, "ShipperCustomer 2 - 서울"),

            // 경기북부
            createShipperDto("d8d5414e-7812-4b01-99e2-1055cbccf676", GYEONGGI_NORTH, "ShipperHub-경기북부"),
            createShipperDto("0cfeddd6-e080-4a1f-b918-b80e0064cf8c", GYEONGGI_NORTH, "ShipperCustomer 1 - 경기북부"),
            createShipperDto("bb659b6b-da76-4cd9-ae54-e3d5dc671910", GYEONGGI_NORTH, "ShipperCustomer 2 - 경기북부"),

            // 경기남부
            createShipperDto("6b4f5a31-5345-45c7-a167-dd1fd2cd6ae8", GYEONGGI_SOUTH, "ShipperHub-경기남부"),
            createShipperDto("fdb75eed-390a-4aad-8bed-611829fba713", GYEONGGI_SOUTH, "ShipperCustomer 1 - 경기남부"),
            createShipperDto("5800e01b-5e60-4261-ade6-20953f7e9d24", GYEONGGI_SOUTH, "ShipperCustomer 2 - 경기남부"),

            // 부산
            createShipperDto("58e95489-8a76-4e46-84b5-ac7ce78e35ca", BUSAN, "ShipperHub-부산"),
            createShipperDto("48410e1f-69c2-41d6-bd6e-892329bff329", BUSAN, "ShipperCustomer 1 - 부산"),
            createShipperDto("ad1ee1d0-53fd-495f-9f35-2cdb2aa6be05", BUSAN, "ShipperCustomer 2 - 부산"),

            // 대구
            createShipperDto("6a26ed86-79f6-46c8-a830-239e9bd45350", DAEGU, "ShipperHub-대구"),
            createShipperDto("fd96ab07-3350-4298-8570-34ca189315ee", DAEGU, "ShipperCustomer 1 - 대구"),
            createShipperDto("afa5f951-02eb-4442-9fa1-363bc8dcec26", DAEGU, "ShipperCustomer 2 - 대구"),

            // 인천
            createShipperDto("0c433c56-4c5d-45f2-989a-8db15f55226a", INCHEON, "ShipperHub-인천"),
            createShipperDto("2e418446-0e43-4af9-b34a-beb1470a87cb", INCHEON, "ShipperCustomer 1 - 인천"),
            createShipperDto("9440bc94-26cb-4a7a-8668-371f19ace9dc", INCHEON, "ShipperCustomer 2 - 인천"),

            // 광주
            createShipperDto("bd1fa464-5b68-4d09-977c-015f3e9c2f6a", GWANGJU, "ShipperHub-광주"),
            createShipperDto("b4c8a76c-df35-4066-8203-3df2455fcf88", GWANGJU, "ShipperCustomer 1 - 광주"),
            createShipperDto("99f26f79-1ed5-4799-b7a0-058a00df55eb", GWANGJU, "ShipperCustomer 2 - 광주"),

            // 대전
            createShipperDto("37d3078c-2a9a-485d-8a2f-09f5c6874e94", DAEJEON, "ShipperHub-대전"),
            createShipperDto("06abaa95-2431-4ff5-8472-5bc17a16a5c3", DAEJEON, "ShipperCustomer 1 - 대전"),
            createShipperDto("b320b1f4-197a-41de-bb68-b882fc0b2870", DAEJEON, "ShipperCustomer 2 - 대전"),

            // 울산
            createShipperDto("531de8b3-2008-4f13-9511-7ee406c2e14a", ULSAN, "ShipperHub-울산"),
            createShipperDto("afa88bdc-0633-4c01-a220-5308ccb5e765", ULSAN, "ShipperCustomer 1 - 울산"),
            createShipperDto("0d8ca948-6881-4dd6-a4a9-79ebe2692d8d", ULSAN, "ShipperCustomer 2 - 울산"),

            // 세종
            createShipperDto("d2bcfe27-a27c-4efb-978a-22c5c674929c", SEJONG, "ShipperHub-세종"),
            createShipperDto("0f343a78-e7a9-47f1-a669-bb301acbc7db", SEJONG, "ShipperCustomer 1 - 세종"),
            createShipperDto("4b933781-8b93-4ebf-bfd1-4ca447cd9416", SEJONG, "ShipperCustomer 2 - 세종"),

            // 강원
            createShipperDto("1d954968-9cad-4526-a1df-5cce4aace3ae", GANGWON, "ShipperHub-강원"),
            createShipperDto("ef88eab0-66a3-43fd-894b-5e3d1bbfc348", GANGWON, "ShipperCustomer 1 - 강원"),
            createShipperDto("eb9169b3-b3d8-4f9b-8e31-95706711fd41", GANGWON, "ShipperCustomer 2 - 강원"),

            // 충북
            createShipperDto("bbb4b3d5-ccfe-467d-a7a5-b5e8a711230a", CHUNGBUK, "ShipperHub-충북"),
            createShipperDto("cf004528-251f-4ced-89f1-2cb6d81c9b58", CHUNGBUK, "ShipperCustomer 1 - 충북"),
            createShipperDto("53e4ad5c-0a53-446b-b31a-11416a4d364e", CHUNGBUK, "ShipperCustomer 2 - 충북")
    );

    private void createShippers() {
        for (ShipperDto shipperDto : shipperDtoList) {
            shipperService.createShipper(shipperDto);
        }
    }

    private ShipperDto createShipperDto(String shipperId, String region, String userName) {
        ShipperTypeEnum shipperType = userName.contains("ShipperHub")
                ? ShipperTypeEnum.SHIPPER_HUB
                : ShipperTypeEnum.SHIPPER_CUSTOMER;

        ShipperDto shipperDto = new ShipperDto();
        shipperDto.setShipperId(UUID.fromString(shipperId));
        shipperDto.setHubId(hubMap.get(region));
        shipperDto.setShipperType(shipperType);

        return shipperDto;
    }
}