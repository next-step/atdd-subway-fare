package nextstep.domain.subway.Fare;

public enum DistanceFareType {

    SECOND_ADDITIONAL_INTERVAL(50L, 8L),
    FIRST_ADDITIONAL_INTERVAL(10L, 5L);

    private final Long thresholdDistance;
    private final Long incrementalDistance;

    DistanceFareType(Long thresholdDistance, Long incrementalDistance) {
        this.thresholdDistance = thresholdDistance;
        this.incrementalDistance = incrementalDistance;
    }

    private static final int ADDITIONAL_FARE_PER_INCREMENTAL_DISTANCE = 100;

    public static int getFareByDistance(Long distance){
        int totalFare = 0 ;
        for(DistanceFareType distanceFareType : values()){
            if(isInThresholdRange(distance, distanceFareType.thresholdDistance)){
                Long addtionalFareDistance = distance - distanceFareType.thresholdDistance;
                totalFare += distanceFareType.calculateOverFare(addtionalFareDistance, distanceFareType.incrementalDistance);

                distance = distanceFareType.thresholdDistance;
            }
        }

        return totalFare;
    }

    public static boolean isInThresholdRange(Long distance,Long thresholdDistance){
        if(distance>thresholdDistance){
            return true;
        }
        return false;
    }

    private static int calculateOverFare(Long distance, Long incrementalDistance) {
        return (int) ((Math.ceil((distance - 1) / incrementalDistance) + 1) * ADDITIONAL_FARE_PER_INCREMENTAL_DISTANCE);
    }
}
