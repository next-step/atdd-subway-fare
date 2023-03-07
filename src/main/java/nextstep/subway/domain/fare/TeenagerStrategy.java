package nextstep.subway.domain.fare;

public class TeenagerStrategy extends AbstractFareStrategy {
    @Override
    public boolean match(int age) {
        return age >= 13 && age < 19;
    }

    @Override
    public int calculateFare(int fare) {
        return calculateFareByWeight(fare, 0.8);
    }
}
