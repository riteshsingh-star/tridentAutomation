package utils;

import com.microsoft.playwright.Page;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTheTimeFormat {

    public static String formatStringTo2Decimal(String valueStr) {
        BigDecimal bd = new BigDecimal(valueStr);
        bd = bd.setScale(2, RoundingMode.DOWN);
        return bd.toPlainString();
    }

    public static double convertToSeconds(String time) {
        Pattern pattern = Pattern.compile("(?:(\\d+)h)?\\s*" + "(?:(\\d+)m)?\\s*" + "(?:(\\d+(?:\\.\\d+)?)s)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(time.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
        double hours = matcher.group(1) != null ? Double.parseDouble(matcher.group(1)) : 0;
        double minutes = matcher.group(2) != null ? Double.parseDouble(matcher.group(2)) : 0;
        double seconds = matcher.group(3) != null ? Double.parseDouble(matcher.group(3)) : 0;
        return (hours * 3600) + (minutes * 60) + seconds;
    }

    public static String convertToUtc(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.of("Asia/Kolkata")).withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    public static String convertBrowserTimeToGMT(Page page, String uiTime) {
        uiTime = uiTime.trim().replaceAll("\\s+", " ");
        String browserZone = (String) page.evaluate("Intl.DateTimeFormat().resolvedOptions().timeZone");
        int year = Year.now().getValue();
        String inputWithYear = year + " " + uiTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM d, HH:mm:ss", Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(inputWithYear, formatter);
        ZonedDateTime browserTime = localDateTime.atZone(ZoneId.of(browserZone));
        ZonedDateTime utcTime = browserTime.withZoneSameInstant(ZoneOffset.UTC);
        return utcTime.toInstant().toString();

    }
}
