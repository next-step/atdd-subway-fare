package nextstep.path.application;

public class PathFareCalculator {

    private final PathFareHandler pathFareCalculator;

    public PathFareCalculator() {
        this.pathFareCalculator = new BaseFareHandler()
                .next(new FirstExtraFareHandler())
                .next(new SecondExtraFareHandler());
    }

    public long calculate(final int distance) {
        return pathFareCalculator.calculate(distance);
    }
}
