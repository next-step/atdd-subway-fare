package nextstep.subway.domain.fare;

public class TeenagerStrategy implements AgeFareStrategy {
    @Override
    public boolean match(int age) {
        return age >= 13 && age < 19;
    }

    @Override
    public double getWeight() {
        return 0.8;
    }
}
