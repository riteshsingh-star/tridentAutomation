package utils;

import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class StatisticsComparison {


    public static double roundOfTheValue(double value) {
        return new BigDecimal(Double.toString(value)).setScale(10, RoundingMode.HALF_UP).doubleValue();
    }

    public static boolean compareStatisticsFromAPIToCalculation(Map<String, Double> statData, Double lcl,
                                                                Double mean, Double stdDev, Double ucl) {
        return Objects.equals(roundOfTheValue(statData.get("mean")), roundOfTheValue(mean))
                && Objects.equals(roundOfTheValue(statData.get("stdDev")), roundOfTheValue(stdDev))
                && Objects.equals(roundOfTheValue(statData.get("uclValue")), roundOfTheValue(ucl)) &&
                Objects.equals(roundOfTheValue(statData.get("lclValue")), roundOfTheValue(lcl));
    }
}
