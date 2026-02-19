package utils;

/**
 * This class represents the result object for
 * Lower Control Limit (LCL) and Upper Control Limit (UCL).
 * It is used to store and transfer calculated or API-retrieved
 * control limit values in KPI/statistical validations.
 */

public class LclUclResult {

    private final String lcl;
    private final String ucl;

    public LclUclResult(String lcl, String ucl) {
        this.lcl = lcl;
        this.ucl = ucl;
    }

    public String getLcl() {
        return lcl;
    }

    public String getUcl() {
        return ucl;
    }
}

