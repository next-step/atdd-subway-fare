package nextstep.subway.domain;

public class PathFareDistance {

    private PathFareDistance() {}

    public static Fare from(final Path path) {
        return path.mergeFare(calculatorFare(path));
    }

    private static Fare calculatorFare(final Path path) {
        return DistanceFarePolicy.from(path).plus(Fare.base());
    }
}
