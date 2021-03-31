package nextstep.subway.path.application;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public Fare calculate(PathResult pathResult) {
        int basicFare = 1250;
        int resultFare = 0;
        int totalDistance = pathResult.getTotalDistance();
        resultFare += basicFare;
        if (totalDistance > 50) {
            int over50Distance = totalDistance - 50;
            int over10Between50Distance = totalDistance - over50Distance - 10;
            return new Fare(resultFare + calculateOverFare(over10Between50Distance, 5) + calculateOverFare(over50Distance, 8));
        }

        if (totalDistance > 10) {
            return new Fare(resultFare + calculateOverFare(totalDistance - 10, 5));
        }
        return new Fare(resultFare);
    }

    private int calculateOverFare(int distance, int perKillo) {
        return (int) ((Math.ceil(distance / perKillo)) * 100);
    }
}
