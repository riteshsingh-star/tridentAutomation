package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This method is used to compare UI graph data with API graph data.
 * It validates:
 * - Map size
 * - Key presence
 * - Value equality (after normalizing decimal values)
 *
 * @param uiMap   Map containing UI graph data (timestamp as key, value as String)
 * @param apiMap  Map containing API graph data (timestamp as key, value as String)
 *
 * @throws AssertionError if any mismatch is found between UI and API data
 */

public class CompareGraphAndApiData {
    public static void compareStringMaps(Map<String, String> uiMap, Map<String, String> apiMap) {
        try {
            List<String> errors = new ArrayList<>();
            if (uiMap.size() != apiMap.size()) {
                errors.add("Size mismatch. UI=" + uiMap.size() + ", API=" + apiMap.size());
            }
            for (Map.Entry<String, String> uiEntry : uiMap.entrySet()) {
                String key = uiEntry.getKey().trim();
                String uiValue = uiEntry.getValue().trim();
                if (!apiMap.containsKey(key)) {
                    errors.add("Missing key in API map: " + key);
                    continue;
                }
                String apiValue = apiMap.get(key).trim();
                uiValue = normalizeDecimal(uiValue);
                apiValue = normalizeDecimal(apiValue);
                if (!uiValue.equals(apiValue)) {
                    errors.add("Value mismatch at [" + key + "] UI=" + uiValue + " API=" + apiValue);
                }
            }
            if (!errors.isEmpty()) {
                throw new AssertionError("UI vs API Map comparison failed:\n" + String.join("\n", errors));
            }
            System.out.println("The UI Graph Data is Matching from API Data");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is used to normalize decimal values to one decimal place
     * before comparing UI and API values.
     *
     * @param val  String representation of numeric value
     * @return     Formatted decimal value up to one decimal place.
     *             Returns original value if parsing fails.
     */

    private static String normalizeDecimal(String val) {
        try {
            return String.format("%.1f", Double.parseDouble(val));
        } catch (Exception e) {
            return val;
        }
    }

}
