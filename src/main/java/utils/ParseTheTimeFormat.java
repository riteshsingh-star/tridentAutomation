package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling time parsing, formatting,
 * numeric rounding, and timezone conversions.
 * Commonly used for:
 * - Converting API timestamps to UI graph format
 * - Formatting decimal values
 * - Converting human-readable duration strings to seconds
 * - Converting IST time to UTC format
 */

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

    /**
     * Formats a numeric string to 2 decimal places
     * using RoundingMode.DOWN.
     * @param valueStr numeric value as String
     * @return formatted string with 2 decimal precision
     */

    public static String formatStringTo2Decimal(String valueStr) {
        BigDecimal bd = new BigDecimal(valueStr);
        bd = bd.setScale(2, RoundingMode.DOWN);
        return bd.toPlainString();
    }
    /**
     * Converts a duration string into total seconds.
     * Supported formats:
     * - "1h 10m 30s"
     * - "10m 5s"
     * - "45s"
     * - "1.5s"
     * @param time human-readable time string
     * @return total duration in seconds
     * @throws IllegalArgumentException if format is invalid
     */

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

    /**
     * Converts a LocalDateTime (assumed IST)
     * into UTC ISO-8601 format with milliseconds.
     * Example output:
     * 2025-01-01T04:30:00.000Z
     * @param dateTime LocalDateTime in IST
     * @return UTC formatted timestamp string
     */

    public static String convertToUtc(LocalDateTime dateTime) {
        return dateTime
                .atZone(ZoneId.of("Asia/Kolkata"))   // change if needed
                .withZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }
}
