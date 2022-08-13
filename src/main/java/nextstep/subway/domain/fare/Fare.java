package nextstep.subway.domain.fare;

import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author a1101466 on 2022/08/09
 * @project subway
 * @description
 */
@ToString
public enum Fare {

    STANDARD(0, 10, 0, 1250),
    OVER_TYPE_1(10, 50, 5, 1250),
    OVER_TYPE_2(50, 9999, 8, 2050)
    ;

    Integer minDistance;
    Integer maxDistance;
    Integer referenceDistance;
    Integer defaultFare;

    private static final Integer STANDARD_DISTANCE = 10;
    private static final Integer OVER_FARE = 100;

    Fare(Integer minDistance, Integer maxDistance, Integer referenceDistance, Integer defaultFare) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.referenceDistance = referenceDistance;
        this.defaultFare = defaultFare;
    }

    public static Integer calculator(int distance){
        Fare distanceType = getType(distance);

        if(isStandard(distanceType))
            return STANDARD.defaultFare;

        int defaultFare = distanceType.defaultFare;
        int overDistance = distance - distanceType.minDistance;
        int referenceDistance = distanceType.referenceDistance;

        return defaultFare + (int) ((Math.ceil((overDistance - 1) / referenceDistance) + 1) * 100);
    }

    private static Fare getType(Integer distance){
        return Arrays.stream(Fare.values())
                .filter(fare -> fare.minDistance <= distance && fare.maxDistance >= distance)
                .findFirst()
                .orElse(STANDARD);
    }

    private static Boolean isStandard(Fare distanceType){
        return Objects.equals(STANDARD, distanceType);
    }

}
