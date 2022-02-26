package nextstep.subway.domain;

import java.math.BigDecimal;

public class PathFare {
    private final BigDecimal fare;

    public PathFare(BigDecimal fare) {
        this.fare = fare;
    }

    public static PathFare of(Path shortestPath, AgeFare ageFare) {
        BigDecimal distanceFare = DistanceFare.extractFare(shortestPath.extractDistance())
                                              .add(shortestPath.extractMaxAdditionalFare());
        BigDecimal extractFare = ageFare.extractDiscountFare(distanceFare);
        return new PathFare(extractFare);
    }

    public BigDecimal getFare() {
        return fare;
    }
}
