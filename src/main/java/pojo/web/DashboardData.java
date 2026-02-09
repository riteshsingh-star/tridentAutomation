package pojo.web;

import java.util.List;

public record DashboardData(
        String dashboardName,
        String dashboardDescription,
        String widgetType,
        String equipmentName,
        String viewType,
        List<String> measuresName,
        String time,
        String granularity,
        String visibilityType
) {
}
