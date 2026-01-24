package com.trident.playwright.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static String normalizeDecimal(String val) {
        try {
            return String.format("%.2f", Double.parseDouble(val));
        } catch (Exception e) {
            return val;
        }
    }

}
