package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class KPISCalculationUtils {

    /**
     * This method verifies aggregate calculations based on tooltip graph data.
     * It supports:
     * - Sum
     * - Average
     * - Cumulative
     * @param aggregateType Type of aggregation (Sum / Average / Cumulative)
     * @param tooltipData   Map containing timestamp as key and value as String
     * @return              Calculated aggregate result
     * @throws IllegalArgumentException if aggregateType is invalid
     */

    public static double verifyTheAggregateData(String aggregateType, Map<String, String> tooltipData) {
        double sum = 0.0;
        int count = 0;
        String lastValue = null;
        for (Map.Entry<String, String> entry : tooltipData.entrySet()) {
            String valueStr = entry.getValue();
            if (valueStr == null || valueStr.isEmpty())
                continue;
            valueStr = valueStr.replace(",", "").replace("%", "").replaceAll("[a-zA-Z]", "").trim();
            try {
                double val = Double.parseDouble(valueStr);
                sum += val;
                count++;
                lastValue = valueStr;
            } catch (NumberFormatException e) {
                System.out.println("Error in verifyTheAggregate: " + valueStr);
            }
        }
        if (aggregateType.equalsIgnoreCase("Sum")) {
            return sum;
        }
        if (aggregateType.equalsIgnoreCase("Average")) {
            return count == 0 ? 0.0 : sum / count;
        }

        if (aggregateType.equals("Cumulative")) {
            return Double.parseDouble(lastValue);
        }

        throw new IllegalArgumentException("Invalid aggregateType: " + aggregateType);
    }


    /**
     * Finds maximum or minimum value from tooltip data map.
     *
     * @param tooltipData Map containing graph values
     * @param maxMinType  "max" to find maximum, "min" to find minimum
     * @return Maximum or minimum value
     * @throws IllegalArgumentException if map is empty
     */

    private static double findMaxAndMin(Map<String, String> tooltipData, String maxMinType) {
        if (tooltipData == null || tooltipData.isEmpty()) {
            throw new IllegalArgumentException("Map is empty");
        }
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (Map.Entry<String, String> entry : tooltipData.entrySet()) {
            double value = Double.parseDouble(entry.getValue());

            if (value > max && maxMinType.equals("max")) {
                return max = value;
            }
            if (value < min && maxMinType.equals("min")) {
                return min = value;
            }
        }
        return 0.0;
    }

    /**
     * This method calculates weighted average based on time duration
     * between consecutive data points.
     *
     * @param data Map containing ISO timestamp as key and numeric value as String
     * @return     Time-weighted average value
     */

    public static double averageByDuration(Map<String, String> data) {

        if (data == null || data.size() < 2) {
            return 0;
        }
        double weightedSum = 0.0;
        long totalSeconds = 0;
        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        Map.Entry<String, String> prev = iterator.next();

        while (iterator.hasNext()) {
            Map.Entry<String, String> curr = iterator.next();
            Instant prevTime = Instant.parse(prev.getKey());
            Instant currTime = Instant.parse(curr.getKey());
            double prevValue = Double.parseDouble(prev.getValue());
            long seconds = Duration.between(prevTime, currTime).getSeconds();
            if (seconds > 0) {
                weightedSum += prevValue * seconds;
                totalSeconds += seconds;
            }
            prev = curr;
        }

        return totalSeconds == 0 ? 0 : weightedSum / totalSeconds;
    }


    /**
     * This method verifies aggregated delta-based KPI values
     * using raw incremental data.
     *
     * @param rawDataMap       Raw data points (timestamp → value)
     * @param aggregatedMap    Aggregated graph values (timestamp → value)
     * @return                 Map of verification results per timestamp
     */

    public static Map<String, VerificationResult> verifyAggregatedData(Map<String, String> rawDataMap, Map<String, String> aggregatedMap) {
        DateTimeFormatter rawFormatter = new DateTimeFormatterBuilder().appendPattern("MMM d, HH:mm:ss").parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear()).toFormatter(Locale.ENGLISH);

        DateTimeFormatter aggFormatter = new DateTimeFormatterBuilder().appendPattern("MMM d, HH:mm:ss").parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear()).toFormatter(Locale.ENGLISH);
        List<RawPoint> rawPoints = new ArrayList<>();
        for (Map.Entry<String, String> entry : rawDataMap.entrySet()) {
            rawPoints.add(new RawPoint(LocalDateTime.parse(entry.getKey(), rawFormatter), Double.parseDouble(entry.getValue())));
        }
        rawPoints.sort(Comparator.comparing(p -> p.time));
        Map<String, VerificationResult> resultMap = new LinkedHashMap<>();
        double epsilon = 0.0001;
        for (Map.Entry<String, String> aggEntry : aggregatedMap.entrySet()) {
            LocalDateTime aggTime = LocalDateTime.parse(aggEntry.getKey(), aggFormatter);
            LocalDateTime windowStart = aggTime.minus(1, ChronoUnit.MINUTES);
            LocalDateTime windowEnd = aggTime;
            double calculatedSum = 0.0;
            RawPoint prev = null;
            RawPoint lastInWindow = null;
            for (RawPoint p : rawPoints) {
                if (p.time.isBefore(windowStart) || p.time.equals(windowStart)) {
                    prev = p;
                    continue;
                }
                if (p.time.isAfter(windowStart) && p.time.isBefore(windowEnd)) {
                    if (prev != null) {
                        calculatedSum += (p.value - prev.value);
                    }
                    prev = p;
                    lastInWindow = p;
                    continue;
                }
                if (p.time.isAfter(windowEnd)) {
                    if (prev != null && lastInWindow != null) {
                        calculatedSum += (p.value - lastInWindow.value);
                    }
                    break;
                }
            }
            double actualValue = Double.parseDouble(aggEntry.getValue());
            boolean isValid = Math.abs(calculatedSum - actualValue) < epsilon;
            resultMap.put(aggEntry.getKey(), new VerificationResult(calculatedSum, actualValue, isValid));
        }
        return resultMap;
    }

    /**
     * This method verifies aggregated sum-based KPI values
     * within a 1-minute time.
     */

    public static Map<String, VerificationResult> verifyAggregatedSumData(Map<String, String> rawDataMap, Map<String, String> aggregatedMap) {
        int year = LocalDate.now().getYear();
        DateTimeFormatter aggFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm:ss yyyy", Locale.ENGLISH);
        List<RawPoint> rawPoints = buildSortedRawPoints(rawDataMap,year);
        Map<String, VerificationResult> resultMap = new LinkedHashMap<>();
        double epsilon = 0.01;
        for (Map.Entry<String, String> aggEntry : aggregatedMap.entrySet()) {
            String aggTimeWithYear = aggEntry.getKey() + " " + year;
            LocalDateTime aggTime = LocalDateTime.parse(aggTimeWithYear, aggFormatter);
            LocalDateTime windowStart = aggTime.minusMinutes(1);
            LocalDateTime windowEnd = aggTime;
            double calculatedSum = 0.0;
            for (RawPoint p : rawPoints) {
                if (p.time.isAfter(windowStart) && !p.time.isAfter(windowEnd)) {
                    calculatedSum += p.value;
                }
            }
            double actualValue = Double.parseDouble(aggEntry.getValue());
            boolean isValid = Math.abs(calculatedSum - actualValue) < epsilon;
            resultMap.put(aggEntry.getKey(), new VerificationResult(calculatedSum, actualValue, isValid));
        }
        return resultMap;
    }

    /**
     * This method verifies aggregated average-based KPI values
     * within a 1-minute time
     */

    public static Map<String, VerificationResult> verifyAggregatedAverageData(Map<String, String> rawDataMap, Map<String, String> aggregatedMap) {
        int year = LocalDate.now().getYear();
        DateTimeFormatter aggFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm:ss yyyy", Locale.ENGLISH);
        List<RawPoint> rawPoints = buildSortedRawPoints(rawDataMap,year);
        Map<String, VerificationResult> resultMap = new LinkedHashMap<>();
        double epsilon = 0.01;
        for (Map.Entry<String, String> aggEntry : aggregatedMap.entrySet()) {
            String aggTimeWithYear = aggEntry.getKey() + " " + year;
            LocalDateTime aggTime = LocalDateTime.parse(aggTimeWithYear, aggFormatter);
            LocalDateTime windowStart = aggTime.minusMinutes(1);
            LocalDateTime windowEnd = aggTime;
            double sum = 0.0;
            int count = 0;
            for (RawPoint p : rawPoints) {
                if (p.time.isAfter(windowStart) && !p.time.isAfter(windowEnd)) {
                    sum += p.value;
                    count++;
                }
            }
            double calculatedAvg = count > 0 ? sum / count : 0.0;
            double actualValue = Double.parseDouble(aggEntry.getValue());
            boolean isValid = Math.abs(calculatedAvg - actualValue) < epsilon;
            resultMap.put(aggEntry.getKey(), new VerificationResult(calculatedAvg, actualValue, isValid));
        }
        return resultMap;
    }

    /**
     * Represents a raw data point.
     */

    public static class RawPoint {
        LocalDateTime time;
        double value;

        RawPoint(LocalDateTime time, double value) {
            this.time = time;
            this.value = value;
        }
    }

    /**
     * Represents the result of KPI verification.
     * - expected = calculated value
     * - actual = API/UI value
     * - isValid = comparison result
     */

    public static class VerificationResult {
        public double expected;
        public double actual;
        public boolean isValid;

        VerificationResult(double expected, double actual, boolean isValid) {
            this.expected = expected;
            this.actual = actual;
            this.isValid = isValid;
        }

        @Override
        public String toString() {
            return "expected=" + expected + ", actual=" + actual + ", match=" + isValid;
        }
    }
    /**
     * Builds and sorts raw data points by timestamp.
     */
    private static List<RawPoint> buildSortedRawPoints(Map<String, String> rawDataMap, int year) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, HH:mm:ss yyyy", Locale.ENGLISH);
        List<RawPoint> rawPoints = new ArrayList<>();
        for (Map.Entry<String, String> entry : rawDataMap.entrySet()) {
            String timeWithYear = entry.getKey() + " " + year;
            LocalDateTime time = LocalDateTime.parse(timeWithYear, formatter);
            double value = Double.parseDouble(entry.getValue());
            rawPoints.add(new RawPoint(time, value));
        }
        rawPoints.sort(Comparator.comparing(p -> p.time));
        return rawPoints;
    }
}

