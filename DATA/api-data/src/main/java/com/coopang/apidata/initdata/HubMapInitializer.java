package com.coopang.apidata.initdata;

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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HubMapInitializer {
    private Map<String, UUID> hubMap;

    public HubMapInitializer() {
        hubMap = new HashMap<>();
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

    public Map<String, UUID> getHubMap() {
        return hubMap;
    }
}
