package nextstep.subway.path.domain;

public class AgeAdolescentFareStrategy implements FareCalculationStrategy {
    private int baseFare;

    public AgeAdolescentFareStrategy(int baseFare) {
        this.baseFare = baseFare;
    }

    @Override
    public int calculate() {
        return (baseFare - AGE_DISCOUNT_DEDUCTION) / 5;
    }
}
