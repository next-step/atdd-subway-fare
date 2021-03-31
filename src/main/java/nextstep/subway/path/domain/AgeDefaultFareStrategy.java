package nextstep.subway.path.domain;

public class AgeDefaultFareStrategy implements FareCalculationStrategy {
    private int baseFare;

    public AgeDefaultFareStrategy(int baseFare) {
        this.baseFare = baseFare;
    }

    @Override
    public int calculate() {
        return baseFare;
    }
}
