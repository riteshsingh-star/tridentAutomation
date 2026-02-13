package config;

public class EnvironmentConfig {

    public static String getBaseUrl() {
        return ReadPropertiesFile.get(ConfigKey.API_BASE_URL);
    }

    public static String getAuthToken() {
        return ReadPropertiesFile.get(ConfigKey.AUTH_TOKEN);
    }

    public static String getOrgId() {
        return ReadPropertiesFile.get(ConfigKey.ORG_ID);
    }

    public static String getUserName() {
        return ReadPropertiesFile.get(ConfigKey.USER_NAME);
    }

    public static String getPassword() {
        return ReadPropertiesFile.get(ConfigKey.WEB_PASSWORD);
    }

    public static String getBrowser() {
        return ReadPropertiesFile.get(ConfigKey.BROWSER);
    }

    public static String getBrowserExecutionMode() {
        return ReadPropertiesFile.get(ConfigKey.HEADLESS);
    }

    public static String getWebURL() {
        return ReadPropertiesFile.get(ConfigKey.WEB_URL);
    }

    public static String getKPIPath() {
        return ReadPropertiesFile.get(ConfigKey.TIME_SERIES_PATH);
    }
    public static String getRawParameterPath() {
        return ReadPropertiesFile.get(ConfigKey.RAW_PARAMETER_PATH);
    }

    public static String getKpiAggregatePath() {
        return ReadPropertiesFile.get(ConfigKey.KPI_AGGREGATE_PATH);
    }
    public static String getEquipmentID() {
        return ReadPropertiesFile.get(ConfigKey.EQUIPMENT_ID);
    }
}
