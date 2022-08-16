package nextstep.subway.domain.policy.discount;

public enum AgePolicy {
    CHILDREN(6,13),
    YOUTH(13, 19);

    private int minAge;
    private int maxAge;

    AgePolicy(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }
}
