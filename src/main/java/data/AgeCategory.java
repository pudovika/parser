package data;

public enum AgeCategory {
    CHILDREN(6,7,90),
    CHILDREN_OLDER(8,9,80),
    CADETS(10,11,70),
    CADETS_OLDER(12,13,50),
    JUNIOR(14,15,40),
    JUNIOR_OLDER(16,17,20),
    ADULTS(18,60,0);

    private int startAge;

    private int endAge;

    private int ratioOffset;

    AgeCategory(int startAge, int endAge, int ratioOffset) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.ratioOffset = ratioOffset;
    }

    public int getStartAge() {
        return startAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public int getRatioOffset() {
        return ratioOffset;
    }

}
