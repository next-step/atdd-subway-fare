package nextstep.member.domain;

public enum AgeType {
    CHILD(0, 13),
    TEENAGER(14, 19),
    ADULT(20, Integer.MAX_VALUE)
    ;
    private final int from;
    private final int to;

    AgeType(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public static AgeType getAgeType(int age) {
        if (age <= CHILD.to) {
            return CHILD;
        } else if (age <= TEENAGER.to) {
            return TEENAGER;
        }
        return ADULT;
    }
}

