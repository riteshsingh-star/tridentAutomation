package com.trident.playwright.utils;

import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ParseTheTimeFormat {

    public static String changeTimeFormatAndValue(String timestamp, double value) {
        String graphTime="";
        String valueStr="";
        try {
            OffsetDateTime odt = OffsetDateTime.parse(timestamp);
            DateTimeFormatter graphFormat =
                    DateTimeFormatter.ofPattern("MMM d, HH:mm:ss", Locale.ENGLISH);
            graphTime = odt
                    .atZoneSameInstant(ZoneId.of("Asia/Kolkata"))
                    .format(graphFormat);
            DecimalFormat df = new DecimalFormat("0.00");
            valueStr = df.format(value);
        }catch (Exception e) {
            System.out.println("Not able to proceed: " + e);
        }
        return graphTime + " " + valueStr;
    }

    public static String changeTimeFormat(String timestamp){
        String uiTime="";
        try {
            OffsetDateTime odt = OffsetDateTime.parse(timestamp);
            DateTimeFormatter uiFormat = DateTimeFormatter.ofPattern("dd MMM HH:mm");
             uiTime= odt.atZoneSameInstant(ZoneId.of("Asia/Kolkata")).format(uiFormat);
        }catch (Exception e) {
            System.out.println("Not able to change the time format: " + e);
        }
        return uiTime;
    }

    public static String formatStringTo2Decimal(String valueStr) {
            double value = Double.parseDouble(valueStr);
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(value);
    }

}
