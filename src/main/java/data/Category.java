package data;

public enum Category {
    LIGHT(90,230),
    LIGHT_MED(230,240),
    MEDIUM_LIGHT(240,250),
    MEDIUM(250,260),
    HEAVY(260,270),
    ABSOLUTE(270,999);

    private final int startRatio;
    private final int endRatio;

    Category(Integer startRatio, Integer endRatio) {
        this.startRatio = startRatio;
        this.endRatio = endRatio;
    }

    public int getStartRatio() {
        return startRatio;
    }

    public int getEndRatio() {
        return endRatio;
    }
}
