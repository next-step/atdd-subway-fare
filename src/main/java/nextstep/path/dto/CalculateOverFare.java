package nextstep.path.dto;

public class CalculateOverFare {

    private static final Long MIN_DISTANCE_OVERFARE = 10L;
    private static final Long SECOND_DISTANCE_OVERFARE = 50L;
    private static final Long DEFAULT_FARE_PRICE = 1250L;
    private static final int OVERFARE_FIRST = 5;
    private static final int OVERFARE_SECOND = 8;
    private static final int DISCOUNT_DISTANCE = 11;

    private CalculateOverFare() {
    }

    public static Path of(Path path) {
        Long totalDistance = calculateOverFare(path.getTotalDistance());
        path.setTotalPrice(totalDistance);
        return path;
    }

    public static Long calculateOverFare(final Long distance) {
        if (distance <= MIN_DISTANCE_OVERFARE) {
            return DEFAULT_FARE_PRICE;
        }
        int overFare = OVERFARE_FIRST;

        if (distance > SECOND_DISTANCE_OVERFARE) {
            overFare = OVERFARE_SECOND;
        }

        return (long) ((Math.ceil((distance - DISCOUNT_DISTANCE) / overFare) + 1) * 100) + DEFAULT_FARE_PRICE;
    }
}

