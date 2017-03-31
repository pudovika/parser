package data;

public enum AgeCategory {
    CHILDREN(6,6,90),
    CHILDREN_OLDER(7,8,80),
    CADETS(9,10,60),
    CADETS_OLDER(11,12,40),
    JUNIOR(13,14,30),
    JUNIOR_OLDER(15,16,20),
    JUNIOR_ADULTS(17,17,20),
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
