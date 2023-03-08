package nextstep.subway.domain.fare;

import java.util.List;


public class FareStrategyFactory {

    private static final List<AgeFareStrategy> strategies = List.of(new FreeStrategy(), new AdultStrategy(), new TeenagerStrategy(), new ChildrenCalculator());

    public static AgeFareStrategy of(int age) {
        return strategies.stream()
                .filter(s -> s.match(age))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
