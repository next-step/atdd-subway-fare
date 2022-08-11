package nextstep.subway.domain.fare;

public class BasicStrategy extends FareStrategy {

    @Override
    public int calculate(int distance) {
        return 1250;
    }
}
