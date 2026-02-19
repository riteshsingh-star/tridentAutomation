package utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.testng.Assert;

/**
 * Utility class responsible for resolving
 * Lower Control Limit (LCL) and Upper Control Limit (UCL)
 * based on the configured type.
 * Supported types:
 * - STATS → Calculated using mean and standard deviation
 * - FIXED → Retrieved directly from API response
 * - NONE  → Not applicable
 */

public class LclUclUtil {

    public static LclUclResult resolveLclUcl(String lclUclType, JsonNode sourceNode, double mean, double stdDev, String logPrefix) {
        String lcl = null;
        String ucl = null;

        String type = lclUclType == null ? "NONE" : lclUclType.toUpperCase();

        switch (type) {
            case "STATS":
                lcl = String.valueOf(Stats.calculateLCL(mean, stdDev));
                ucl = String.valueOf(Stats.calculateUCL(mean, stdDev));
                break;

            case "FIXED":
                lcl = sourceNode.path("lcl").asText(null);
                ucl = sourceNode.path("ucl").asText(null);
                break;

            case "NONE":
                System.out.println(logPrefix + " LCL/UCL not applicable");
                break;

            default:
                Assert.fail("Unknown lclUclType: " + lclUclType);
        }

        if (!"NONE".equals(type)) {
            Assert.assertNotNull(lcl, "LCL should not be null");
            Assert.assertNotNull(ucl, "UCL should not be null");
        }

        System.out.println("Final " + logPrefix + " LCL: " + lcl);
        System.out.println("Final " + logPrefix + " UCL: " + ucl);

        return new LclUclResult(lcl, ucl);
    }
}

