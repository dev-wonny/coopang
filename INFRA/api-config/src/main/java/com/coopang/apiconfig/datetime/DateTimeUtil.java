package com.coopang.apiconfig.datetime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    // private 생성자로 외부에서 인스턴스화 방지
    private DateTimeUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * String을 LocalDateTime으로 변환
     *
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime parseToLocalDateTime(String dateTimeString) {
        try {
            if (!StringUtils.isBlank(dateTimeString)) {
                return LocalDateTime.parse(dateTimeString, FORMATTER);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm:ss", e);
        }
        return null;
    }

    /**
     * LocalDateTime을 String으로 변환
     *
     * @param dateTime
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        try {
            if (!ObjectUtils.isEmpty(dateTime)) {
                return dateTime.format(FORMATTER);
            }
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd'T'HH:mm:ss", e);
        }
        return null;
    }
}