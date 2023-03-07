package nextstep.subway.domain.fare;

public class ChildrenCalculator implements AgeFareStrategy {
    @Override
    public boolean match(int age) {
        return age >= 6 && age < 13;
    }

    @Override
    public double getWeight() {
        return 0.5;
    }
}
