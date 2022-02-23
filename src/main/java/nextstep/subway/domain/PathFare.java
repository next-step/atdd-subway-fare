package nextstep.subway.domain;

import java.math.BigDecimal;

public class PathFare {
    private Path shortestPath;
    private AgeFare ageFare;

    public PathFare(Path shortestPath, AgeFare ageFare) {
        this.shortestPath = shortestPath;
        this.ageFare = ageFare;
    }

    public BigDecimal extractFare() {
        BigDecimal distanceFare = DistanceFare.extractFare(shortestPath.extractDistance())
                                              .add(shortestPath.extractMaxAdditionalFare());
        return ageFare.extractDiscountFare(distanceFare);
    }
}
