package pojo.api;

import java.util.List;

public record Kpis(

        int id,
        List<Integer> kpiParamDefIds
) {

}
