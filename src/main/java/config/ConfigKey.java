package config;

import lombok.Getter;

@Getter
public enum ConfigKey {

    API_BASE_URL("APIBaseURL"),
    AUTH_TOKEN("authToken"),
    ORG_ID("X-ORG-ID"),
    BROWSER("browser"),
    HEADLESS("headless"),
    WEB_URL("webUrl"),
    USER_NAME("userName"),
    WEB_PASSWORD("webPassword"),
    TIME_SERIES_PATH("timeSeriesPathURL"),
    RAW_PARAMETER_PATH("rawParameterPathURL"),
    KPI_AGGREGATE_PATH("kpiAggregatePathURL"),
    EQUIPMENT_ID("equipmentID");

    private final String key;

    ConfigKey(String key) {
        this.key = key;
    }

}
