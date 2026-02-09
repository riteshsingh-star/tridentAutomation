package pojo.api;

import java.util.List;

public record EquipKpi(

        int id,
        List<Integer> kpiParamDefIds
) {

}
