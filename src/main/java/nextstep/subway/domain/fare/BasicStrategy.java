package nextstep.subway.domain.fare;

public class BasicStrategy implements FareStrategy {

    @Override
    public int calculate(int distance) {
        return 1250;
    }
}
