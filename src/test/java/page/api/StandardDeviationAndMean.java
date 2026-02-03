package page.api;

import base.api.APIBase;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;

public class StandardDeviationAndMean extends APIBase {

    @Test
    public static void testCalculateStandardDeviation() throws IOException {
        Map<String, String> rawParameterData = GetRawParameterData.getRawParameterDataValues();
        double standardDeviation = APIBase.calculateSTDdiv(rawParameterData);
        double sum = 0.0;
        int count = 0;
        for (String valueStr : rawParameterData.values()) {
            try {
                double value = Double.parseDouble(valueStr);
                sum += value;
                count++;
            } catch (NumberFormatException e) {
                continue;
            }
        }
        double mean = count > 0 ? sum / count : 0.0;
        double stdLong =standardDeviation;
        double meanLong = mean;
        double ucl = APIBase.calculateUCL(stdLong, meanLong);
        double lcl = APIBase.calculateLCL(stdLong, meanLong);

        System.out.println("Mean value: " +  mean);
        System.out.println("Standard deviation: " + standardDeviation);
        System.out.println("UCL : " + ucl);
        System.out.println("LCL : " + lcl);
        
        System.out.println("--- Sample Data Points ---");
        int sampleCount = 0;
        for (Map.Entry<String, String> entry : rawParameterData.entrySet()) {
            if (sampleCount < 5) {
                System.out.println("Timestamp: " + entry.getKey() + " | Value: " + entry.getValue());
                sampleCount++;
            } else {
                break;
            }
        }
        
        Assert.assertTrue(standardDeviation >= 0.0, "Standard deviation should be non-negative");
        
        if (rawParameterData.size() > 1) {
            Assert.assertTrue(standardDeviation > 0.0, "Standard deviation should be positive for multiple data points");
        }
        
        System.out.println("\nTotal data points processed: " + rawParameterData.size());
    }

}
