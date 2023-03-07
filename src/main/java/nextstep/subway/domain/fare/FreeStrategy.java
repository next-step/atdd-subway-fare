package nextstep.subway.domain.fare;

public class FreeStrategy implements AgeFareStrategy {
    @Override
    public boolean match(int age) {
        return age < 6;
    }

    @Override
    public double getWeight() {
        return 0;
    }
}
