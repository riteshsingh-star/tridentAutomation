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
        System.out.println("\n--- Sample Data Points ---");
        int count = 0;
        for (Map.Entry<String, String> entry : rawParameterData.entrySet()) {
            if (count < 5) {
                System.out.println("Timestamp: " + entry.getKey() + " | Value: " + entry.getValue());
                count++;
            } else {
                break;
            }
        }
        Assert.assertTrue(standardDeviation >= 0.0, "Standard deviation should be non-negative");
        
        if (rawParameterData.size() > 1) {
            Assert.assertTrue(standardDeviation > 0.0, "Standard deviation should be positive for multiple data points");
        }
        System.out.println("standard deviation: " + String.format("%.6f", standardDeviation));
    }

}
