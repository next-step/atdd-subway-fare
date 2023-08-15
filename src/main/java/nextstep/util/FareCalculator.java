package nextstep.util;

import nextstep.domain.subway.Fare.DistanceFarePolicy;
import nextstep.domain.subway.Fare.FarePolicy;
import nextstep.domain.subway.Fare.LineFarePolicy;
import nextstep.domain.subway.Line;

import java.util.List;

public class FareCalculator {
    private static final int BASE_FARE = 1250;
    public static int totalFare(Long distance , List<Line> lines){
        int totalFare = BASE_FARE;

        FarePolicy farePolicy = new DistanceFarePolicy(distance)
            .setNextFarePolicy(new LineFarePolicy(lines));

        totalFare = farePolicy.getCalculatedFare(totalFare);

        return totalFare;
    }

}
