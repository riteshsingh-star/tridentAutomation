package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTheTimeFormat {

    public static String changeTimeFormat(String timestamp) {
        String graphTime = "";
        try {
            OffsetDateTime odt = OffsetDateTime.parse(timestamp);
            DateTimeFormatter graphFormat =
                    DateTimeFormatter.ofPattern("MMM d, HH:mm:ss", Locale.ENGLISH);
            graphTime = odt.atZoneSameInstant(ZoneId.of("Asia/Kolkata")).format(graphFormat);
        } catch (Exception e) {
            System.out.println("Not able to proceed: " + e);
        }
        return graphTime;
    }

    public static String formatStringTo2Decimal(String valueStr) {
        BigDecimal bd = new BigDecimal(valueStr);
        bd = bd.setScale(1, RoundingMode.DOWN);
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
}
