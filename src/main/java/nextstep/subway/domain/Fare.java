package nextstep.subway.domain;

import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author a1101466 on 2022/08/09
 * @project subway
 * @description
 */
@ToString
public enum Fare {

    STANDARD(0, 10, 0),
    OVER_TYPE_1(11, 50, 5),
    OVER_TYPE_2(51, 9999, 8)
    ;

    Integer minDistance;
    Integer maxDistance;
    Integer referenceDistance;

    private static final Integer STANDARD_DISTANCE = 10;
    private static final Integer DEFAULT_FARE = 1250;
    private static final Integer OVER_FARE = 100;

    Fare(Integer minDistance, Integer maxDistance, Integer referenceDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.referenceDistance = referenceDistance;
    }

    public static Integer calculator(int distance){
        if(STANDARD.maxDistance >= distance)
            return DEFAULT_FARE;

        int overDistance = distance -STANDARD_DISTANCE;
        int referenceDistance = getReferenceDistance(distance);

        return DEFAULT_FARE + (int) ((Math.ceil((overDistance - 1) / referenceDistance) + 1) * 100);
    }

    public static Integer getReferenceDistance(Integer distance){
        return Arrays.stream(Fare.values())
                .filter(fare -> fare.minDistance <= distance && fare.maxDistance >= distance)
                .findFirst()
                .map(fare -> fare.referenceDistance)
                .orElse(STANDARD.referenceDistance);
    }

}
