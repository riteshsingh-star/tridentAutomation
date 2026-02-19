package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiHelper {

    static String changedDoubleValue;

    /**
     * This method is used to fetch API graph data from the response JSON.
     *
     * @param responseJson           Complete API response in String format
     * @param rootPath               Root path key in JSON (example: "equipments")
     * @param parameterName          Parameter name to extract data from
     * @param isComparingWebGraph    Boolean flag to format value to 2 decimals when comparing with web graph
     * @return                       Map containing formatted timestamp as key and value as String
     * @throws JsonProcessingException if JSON parsing fails
     */

    public static Map<String, String> fetchApiData(String responseJson, String rootPath, String parameterName, boolean isComparingWebGraph) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseJson);
        JsonNode dataArray = root.path(rootPath).path(0).path(parameterName).path(0).path("data");
        Map<String, String> apiValues = new LinkedHashMap<>();
        Iterator<JsonNode> iterator = dataArray.elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            String gmtTimestamp = node.get("timestamp").asText();
            JsonNode valueNode = node.get("doubleValue"); //Double, we cannot, as it is breaking the null comparison
            if (!valueNode.isNull()) {
                String value = valueNode.asText(); //gson
                //String convertedTimeStamp = ParseTheTimeFormat.changeTimeFormat(gmtTimestamp);
                if (isComparingWebGraph) {
                    changedDoubleValue = ParseTheTimeFormat.formatStringTo2Decimal(value);
                    apiValues.put(gmtTimestamp, changedDoubleValue);
                } else {
                    apiValues.put(gmtTimestamp, value);
                }
            }
        }
        return apiValues;
    }

    /**
     * This method is used to fetch statistical data (max, min, mean, median, std deviation, LCL, UCL)
     * or single value data from the API response.
     *
     * @param rootNode  Root JsonNode of the API response
     * @return          Map containing statistical values with corresponding keys
     */

    public static Map<String, Double> fetchStatisticsData(JsonNode rootNode) {
        Map<String, Double> s = new LinkedHashMap<>();
        JsonNode equipmentsArray = rootNode.has("equipments") ? rootNode.path("equipments") : rootNode.path("equipKpis");
        for (JsonNode equip : equipmentsArray) {
            int eqId = equip.path("id").asInt();
            JsonNode paramsArray = equip.has("rawParameters") ? equip.path("rawParameters") : equip.path("kpis");
            for (JsonNode param : paramsArray) {
                int paramId = param.has("rawParamDefId") ? param.path("rawParamDefId").asInt() : param.path("kpiDefParamId").asInt();
                JsonNode stats = param.path("statistics");
                JsonNode dataNode = param.path("data");
                if (!stats.isMissingNode() && stats.has("maxValue")) {
                    s.put("max", stats.path("maxValue").asDouble());
                    s.put("min", stats.path("minValue").asDouble());
                    s.put("mean", stats.path("meanValue").asDouble());
                    s.put("median", stats.path("medianValue").asDouble());
                    s.put("stdDev", stats.path("standardDeviationValue").asDouble());
                    s.put("lclValue", stats.path("lcl").asDouble());
                    s.put("uclValue", stats.path("ucl").asDouble());
                } else if (!dataNode.isMissingNode()) {
                    s.put("value", dataNode.path("value").asDouble());
                }

            }
        }
        return s;
    }

    /**
     * This method is used to fetch KPI aggregate data (value and type)
     * from the first equipment and first KPI.
     *
     * @param rootNode  Root JsonNode of the API response
     * @return          Map containing KPI value and type, or null if not available
     */

    public static Map<String, Object> getKpiAggregateData(JsonNode rootNode) {
        JsonNode kpiNode = rootNode.path("equipments").path(0).path("kpis").path(0);
        if (!kpiNode.isMissingNode()) {
            JsonNode dataNode = kpiNode.path("data");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("value", dataNode.path("value").asDouble());
            result.put("type", dataNode.path("type").asText());
            return result;
        }
        return null;
    }
}
