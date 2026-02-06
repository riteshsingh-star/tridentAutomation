package pojo.api;

import java.util.List;

public record RewMain(

        List<Raws> equipKpis,
        DateRange dateRange,
        long granularityInMillis,
        boolean addMissingTimestamps
) {
}
