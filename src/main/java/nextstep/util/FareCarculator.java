package nextstep.util;

public class FareCarculator {

    private static final int BASE_FARE = 1250;
    private static final int MAX_FIRST_ADDITIONAL_FARE = 800;
    private static final int ADDITIONAL_FARE_PER_INTERVAL = 100;

    private static final Long FIRST_ADDITIONAL_FARE_DISTANCE = 10L;
    private static final Long SECOND_ADDITIONAL_FARE_DISTANCE = 50L;

    private static final Long FIRST_ADDITIONAL_FARE_INTERVAL = 5L;
    private static final Long SECOND_ADDITIONAL_FARE_INTERVAL = 8L;




    public static int totalFare(Long distance){
        int totalFare = 0;
        if(distance<=FIRST_ADDITIONAL_FARE_DISTANCE){
            totalFare += BASE_FARE;
            return totalFare;
        }
         if(distance>FIRST_ADDITIONAL_FARE_DISTANCE && distance<=SECOND_ADDITIONAL_FARE_DISTANCE){
            totalFare += BASE_FARE;
            totalFare += calculateOverFare(distance - FIRST_ADDITIONAL_FARE_DISTANCE, FIRST_ADDITIONAL_FARE_INTERVAL);
            return totalFare;
        }
         if(distance>SECOND_ADDITIONAL_FARE_DISTANCE){
            totalFare += BASE_FARE + MAX_FIRST_ADDITIONAL_FARE;
            totalFare += calculateOverFare(distance -SECOND_ADDITIONAL_FARE_DISTANCE, SECOND_ADDITIONAL_FARE_INTERVAL);

            return totalFare;
        }

        throw new IllegalArgumentException("정의되지 않은 요금구간입니다.");
    }

    private static int calculateOverFare(Long distance, Long interval) {
        return (int) ((Math.ceil((distance - 1) / interval) + 1) * ADDITIONAL_FARE_PER_INTERVAL);
    }
}
