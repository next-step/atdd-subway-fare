package nextstep.subway.domain;

public class PathFareDistance {

    public static Fare of(final Path path) {
        return calculatorFare(path);
    }

    private static Fare calculatorFare(final Path path) {
        return DistanceFarePolicy.from(path).plus(Fare.BASE_FARE);
    }

    private PathFareDistance() {}
}
