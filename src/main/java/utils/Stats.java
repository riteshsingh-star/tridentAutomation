package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Stats {

    public static double calculateMean(Map<String, String> rawParameterData) {
        double sum = 0.0;
        int count = 0;

        for (String valueStr : rawParameterData.values()) {
            try {
                sum += Double.parseDouble(valueStr);
                count++;
            } catch (Exception ignored) {}
        }
        return count > 0 ? sum / count : 0.0;
    }


    public static double calculateStdDev(Map<String, String> rawParameterData) {
        List<Double> values = new ArrayList<>();

        for (String valueStr : rawParameterData.values()) {
            try {
                values.add(Double.parseDouble(valueStr));
            } catch (Exception ignored) {}
        }

        if (values.size() <= 1) return 0.0;

        double mean = calculateMean(rawParameterData);

        double varianceSum = 0.0;
        for (double v : values) {
            varianceSum += Math.pow(v - mean, 2);
        }

        double variance = varianceSum / (values.size() - 1);
        return Math.sqrt(variance);
    }

    public static double calculateUCL(double mean, double stdDev) {
        return mean + (3 * stdDev);
    }

    public static double calculateLCL(double mean, double stdDev) {
        return mean - (3 * stdDev);
    }
}
