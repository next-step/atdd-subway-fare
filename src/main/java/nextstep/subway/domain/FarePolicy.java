package nextstep.subway.domain;

public enum FarePolicy {
    DEFAULT {
        @Override
        protected int calculate(int distance) {
            return DEFAULT_FARE;
        }
    },
    TEN_KILO {
        @Override
        protected int calculate(int distance) {
            return DEFAULT_FARE + calculateOverFare(distance - TEN_KILO_METER, EVERY_5KM);
        }
    },
    FIFTY_KILO {
        @Override
        protected int calculate(int distance) {
            return DEFAULT_FARE + calculateOverFare(FIFTY_KILO_METER - TEN_KILO_METER, EVERY_5KM)
                    + calculateOverFare(distance - FIFTY_KILO_METER, EVERY_8KM);
        }
    };

    public static final int DEFAULT_FARE = 1250;
    public static final int TEN_KILO_METER = 10;
    public static final int FIFTY_KILO_METER = 50;
    public static final int EVERY_5KM = 5;
    public static final int EVERY_8KM = 8;

    public static int calculateFare(int distance) {
        if (distance < TEN_KILO_METER) {
            return DEFAULT.calculate(distance);
        }
        if (distance < FIFTY_KILO_METER) {
            return TEN_KILO.calculate(distance);

        }
        return FIFTY_KILO.calculate(distance);
    }

    protected int calculateOverFare(int distance, int overKiloMeter) {
        return (int) ((Math.ceil((distance - 1) / overKiloMeter) + 1) * 100);
    }

    protected abstract int calculate(int distance);
}
