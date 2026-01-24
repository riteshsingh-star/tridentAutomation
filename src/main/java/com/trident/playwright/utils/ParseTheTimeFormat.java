package com.trident.playwright.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ParseTheTimeFormat {

    public static String changeTimeFormat(String timestamp) {
        String graphTime="";
        try {
            OffsetDateTime odt = OffsetDateTime.parse(timestamp);
            DateTimeFormatter graphFormat =
                    DateTimeFormatter.ofPattern("MMM d, HH:mm:ss", Locale.ENGLISH);
            graphTime = odt.atZoneSameInstant(ZoneId.of("Asia/Kolkata")).format(graphFormat);
        }catch (Exception e) {
            System.out.println("Not able to proceed: " + e);
        }
        return graphTime;
    }

    public static String formatStringTo2Decimal(String valueStr) {
        BigDecimal bd = new BigDecimal(valueStr);
        bd = bd.setScale(1, RoundingMode.DOWN);
        return bd.toPlainString();
    }

}
