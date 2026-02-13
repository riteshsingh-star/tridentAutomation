package pojo.api;

import java.util.List;

public record KpiAggregateRequest(
        List<EquipKpi> equipments,
        DateRange dateRange
) {
}
