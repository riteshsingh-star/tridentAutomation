package pojo;

public class RawParamInfo {

    private final int rawParamEquipmentId;
    private final int rawParamId;

    public RawParamInfo(int rawParamEquipmentId, int rawParamId) {
        this.rawParamEquipmentId = rawParamEquipmentId;
        this.rawParamId = rawParamId;
    }

    public int getRawParamEquipmentId() {
        return rawParamEquipmentId;
    }

    public int getRawParamId() {
        return rawParamId;
    }
}
