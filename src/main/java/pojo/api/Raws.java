package pojo.api;

import java.util.List;

public record Raws(
        int id,
        List<Integer> rawParamDefIds
) {
}
