package nextstep.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FareType {

    OVER_TYPE2(50, 8, 100),
    OVER_TYPE1(10, 5, 100),
    STANDARD(0, 1, 0),
    ;
    
    private final int startDistance;
    private final int perDistance;
    private final int perFee;

    private static final int DEFAULT_FARE = 1250;
    private static final int FEE = 1250;
    
    public static int calculateFare(final int distance) {
        int fare = 0;
        int remainDistance = distance;

        for (final FareType fareType : FareType.values()) {
            if (isUnreachedFareType(remainDistance, fareType)) {
                continue;
            }

            fare += calculateFare(remainDistance, fareType);
            remainDistance = fareType.getStartDistance();
        }

        return fare + DEFAULT_FARE;
    }

    private static boolean isUnreachedFareType(final int distance, final FareType fareType) {
        return distance <= fareType.getStartDistance();
    }

    private static double calculateFare(final int distance, final FareType fareType) {
        return calculateDistance(distance, fareType) * fareType.getPerFee();
    }

    private static double calculateDistance(final int distance, final FareType fareType) {
        return Math.ceil((distance - fareType.getStartDistance() - 1) / fareType.getPerDistance()) + 1;
    }

}
