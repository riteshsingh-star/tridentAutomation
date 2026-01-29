package pojo;

public record EquipmentMeasureValidation(
        String equipmentName,
        String measureName,
        String frequency,
        String granularity,
        String aggregateType
) {
}
