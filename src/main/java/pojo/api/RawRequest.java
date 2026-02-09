package pojo.api;

import java.util.List;

public record RawRequest(

        List<Raws> equipments,
        DateRange dateRange,
        long granularityInMillis,
        boolean addMissingTimestamps
) {
}
