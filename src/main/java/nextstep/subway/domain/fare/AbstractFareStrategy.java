package nextstep.subway.domain.fare;

public abstract class AbstractFareStrategy implements AgeFareStrategy {

    private static final int FIXED_DISCOUNT = 350;

    protected int calculateFareByWeight(int fare, double weight) {
        return (int) ((fare - FIXED_DISCOUNT) * weight);
    }
}
