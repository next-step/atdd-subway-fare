package nextstep.subway.domain.fare;

public class AdultStrategy implements AgeFareStrategy {
    @Override
    public boolean match(int age) {
        return age >= 20;
    }

    @Override
    public double getWeight() {
        return 1;
    }
}
