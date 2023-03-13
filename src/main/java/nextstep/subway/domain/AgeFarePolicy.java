package nextstep.subway.domain;

public class AgeFarePolicy implements FarePolicy<Path> {
    private static final int DEDUCTION = 350;

    private final int age;

    public AgeFarePolicy(final Integer age) {
        this.age = age;
    }

    @Override
    public int apply(final Path value, final int baseFare) {
        return AgeGroup.calculateFare(age, baseFare - DEDUCTION) + DEDUCTION;
    }
}
