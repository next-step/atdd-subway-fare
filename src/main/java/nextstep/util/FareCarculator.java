package nextstep.util;

import nextstep.domain.subway.Fare.DistanceFarePolicy;

public class FareCarculator {

    public static int totalFare(Long distance){
        return DistanceFarePolicy.getFareByDistance(distance);
    }

}
