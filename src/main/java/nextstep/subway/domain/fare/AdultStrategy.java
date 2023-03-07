package nextstep.subway.domain.fare;

public class AdultStrategy extends AbstractFareStrategy {
    @Override
    public boolean match(int age) {
        return age >= 20;
    }

    @Override
    public int calculateFare(int fare) {
        return fare;
    }
}
