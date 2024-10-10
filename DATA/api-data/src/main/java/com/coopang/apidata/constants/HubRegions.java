package com.coopang.apidata.constants;

public class HubRegions {
    public static final String SEOUL = "서울";
    public static final String GYEONGGI_NORTH = "경기북부";
    public static final String GYEONGGI_SOUTH = "경기남부";
    public static final String BUSAN = "부산";
    public static final String DAEGU = "대구";
    public static final String INCHEON = "인천";
    public static final String GWANGJU = "광주";
    public static final String DAEJEON = "대전";
    public static final String ULSAN = "울산";
    public static final String SEJONG = "세종";
    public static final String GANGWON = "강원";
    public static final String CHUNGBUK = "충북";

    public static String[] getHubRegions() {
        return new String[] {
                HubRegions.SEOUL,
                HubRegions.GYEONGGI_NORTH,
                HubRegions.GYEONGGI_SOUTH,
                HubRegions.BUSAN,
                HubRegions.DAEGU,
                HubRegions.INCHEON,
                HubRegions.GWANGJU,
                HubRegions.DAEJEON,
                HubRegions.ULSAN,
                HubRegions.SEJONG,
                HubRegions.GANGWON,
                HubRegions.CHUNGBUK
        };
    }
}
