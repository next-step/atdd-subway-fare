package nextstep.path.application.fare.discount.age;

public class AgeRange {

    private final int startInclusive;
    private final int endExclusive;

    private AgeRange(final int startInclusive, final int endExclusive) {
        validate(startInclusive, endExclusive);
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    private void validate(final int startInclusive, final int endExclusive) {
        if(startInclusive < 0 || endExclusive < 0) {
            throw new IllegalArgumentException("age range must be positive");
        }
    }

    public static AgeRange of(final int startInclusive, final int endExclusive) {
        return new AgeRange(startInclusive, endExclusive);
    }

    public boolean contains(final int age) {
        return startInclusive <= age && age < endExclusive;
    }
}
