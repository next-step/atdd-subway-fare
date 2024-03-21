package nextstep.auth.domain;

public enum AgeType {
    CHILDREN(1, 12),
    TEENAGE(13, 18),
    ADULT(19, Integer.MAX_VALUE);

    private final int minAge;
    private final int maxAge;

    AgeType(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public boolean isInRange(int age) {
        return age >= minAge && age <= maxAge;
    }
}

