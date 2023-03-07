package nextstep.subway.domain.fare;

public class FreeStrategy extends AbstractFareStrategy {
    @Override
    public boolean match(int age) {
        return age < 6;
    }

    @Override
    public int calculateFare(int fare) {
        return 0;
    }
}
