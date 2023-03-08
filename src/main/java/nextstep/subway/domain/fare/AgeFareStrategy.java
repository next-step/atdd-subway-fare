package nextstep.subway.domain.fare;

public interface AgeFareStrategy {
    boolean match(int age);

    double getWeight();
}
