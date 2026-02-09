package pojo.web;

public record EquipmentMeasureValidation(
        String equipmentName,
        String measureName,
        String frequency,
        String granularity,
        String aggregateType
) {
}
