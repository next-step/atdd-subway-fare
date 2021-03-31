package nextstep.subway.path.domain;

public class AgeChildFareStrategy implements FareCalculationStrategy {
    private int baseFare;

    public AgeChildFareStrategy(int baseFare) {
        this.baseFare = baseFare;
    }

    @Override
    public int calculate() {
        return (baseFare - AGE_DISCOUNT_DEDUCTION) / 2;
    }
}
