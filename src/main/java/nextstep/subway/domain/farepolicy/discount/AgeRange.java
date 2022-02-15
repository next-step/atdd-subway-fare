package nextstep.subway.domain.farepolicy.discount;

public class AgeRange {
    private final int minAge;
    private final int maxAge;

    public AgeRange(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public boolean isWithin(int age) {
        return age >= minAge && age < maxAge;
    }
}
