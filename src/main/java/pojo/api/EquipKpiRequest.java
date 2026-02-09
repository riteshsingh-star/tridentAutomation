package pojo.api;

import java.util.List;

public record EquipKpiRequest(
        List<EquipKpi> equipKpis,
        DateRange dateRange,
        int granularityInMillis,
        boolean addMissingTimestamps
) {
}
