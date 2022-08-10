package nextstep.subway.domain.fare;

@FunctionalInterface
public interface FareStrategy {
    int calculate(int distance);
}
