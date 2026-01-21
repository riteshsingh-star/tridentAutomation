package com.trident.playwright.utils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ParseTheTimeFormat {

    public static String changeTimeFormat(String timestamp){
        OffsetDateTime odt = OffsetDateTime.parse(timestamp);

        DateTimeFormatter uiFormat =
                DateTimeFormatter.ofPattern("dd MMM HH:mm");

        String uiTime = odt
                .atZoneSameInstant(ZoneId.of("Asia/Kolkata"))
                .format(uiFormat);

        return uiTime;
    }
}
