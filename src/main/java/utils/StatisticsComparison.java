package utils;

import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for comparing statistical values
 * returned from API responses against locally calculated values.
 * This ensures precision-safe comparison by rounding values
 * before validating equality.
 */

public class StatisticsComparison {

    /**
     * Rounds a double value to 10 decimal places
     * using HALF_UP rounding mode.
     * This avoids floating-point precision issues
     * during statistical comparisons.
     * @param value numeric value to round
     * @return rounded double value
     */

    public static double roundOfTheValue(double value) {
        return new BigDecimal(Double.toString(value)).setScale(10, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Compares API statistical values against calculated values.
     * Expected keys in statData map:
     * - "mean"
     * - "stdDev"
     * - "uclValue"
     * - "lclValue"
     * All values are rounded before comparison to
     * eliminate floating-point mismatch issues.
     * @param statData Map containing API statistical values
     * @param lcl      Calculated Lower Control Limit
     * @param mean     Calculated Mean
     * @param stdDev   Calculated Standard Deviation
     * @param ucl      Calculated Upper Control Limit
     * @return true if all statistical values match after rounding
     */

    public static boolean compareStatisticsFromAPIToCalculation(Map<String, Double> statData, Double lcl,
                                                                Double mean, Double stdDev, Double ucl) {
        return Objects.equals(roundOfTheValue(statData.get("mean")), roundOfTheValue(mean))
                && Objects.equals(roundOfTheValue(statData.get("stdDev")), roundOfTheValue(stdDev))
                && Objects.equals(roundOfTheValue(statData.get("uclValue")), roundOfTheValue(ucl)) &&
                Objects.equals(roundOfTheValue(statData.get("lclValue")), roundOfTheValue(lcl));
    }
}
