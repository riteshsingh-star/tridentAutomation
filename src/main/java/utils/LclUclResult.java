package utils;

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

