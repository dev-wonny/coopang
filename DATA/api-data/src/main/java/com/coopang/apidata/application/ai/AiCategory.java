package com.coopang.apidata.application.ai;

public enum AiCategory {
    TODAY_WEATHER
    , TODAY_SHIPPER_SUMMARY_DELIVERY
    , TODAY_HUB_SUMMARY_ORDER
    ;

    public static AiCategory getAiCategoryEnum(String s) {
        try {
            return AiCategory.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Ai Category: " + s);
        }
    }
}