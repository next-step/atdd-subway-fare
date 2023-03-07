package nextstep.subway.domain.fare;

public class ChildrenStrategy extends AbstractFareStrategy {
    @Override
    public boolean match(int age) {
        return age >= 6 && age < 13;
    }

    @Override
    public int calculateFare(int fare) {
        return calculateFareByWeight(fare, 0.5);
    }
}
