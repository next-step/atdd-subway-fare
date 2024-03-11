package nextstep.subway.domain;

import java.util.List;

public class FareCalculator {
    private PathFinder pathFinder;

    public FareCalculator(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public int getFare(Long source, Long target) {
        return fare;
    }
}
