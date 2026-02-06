package pojo.api;

import java.util.List;

public record KpiMain(
        List<Kpis> equipKpis,
        DateRange dateRange,
        int granularityInMillis,
        boolean addMissingTimestamps
) {
}
