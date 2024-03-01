package nextstep.subway.domain.path;

public class Fare {

    private static final int MINIMUM_VALUE = 0;
    private static final int DEFAULT_FARE = 1250;
    private final int value;

    public Fare(final int distance) {
        validationCheck(distance);

        final int TenKmOverFare = calculate10KmOverFare(distance - 10);
        final int fiftyOverFare = calculate50kmOverFare(distance - 50);

        this.value = DEFAULT_FARE + TenKmOverFare + fiftyOverFare;
    }

    private void validationCheck(final int distance) {
        if (distance < MINIMUM_VALUE) {
            throw new IllegalArgumentException("요금 계산을 위한 거리가 음수 일 수 없습니다 : " + distance);
        }
    }

    private int calculate10KmOverFare(int distance) {
        if (distance > 0 && distance < 41) {
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }

        if (distance >= 41) {
            return 800;
        }

        return 0;
    }

    private int calculate50kmOverFare(int distance) {
        if (distance > 0) {
            return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }

        return 0;
    }

    public int value() {
        return this.value;
    }
}

