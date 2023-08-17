package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public class DistanceFarePolicy extends FarePolicy {
    private static final int BASE_FARE = 1250;
    private static final int SUBURBAN_FARE = 2050;
    private int distance;

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    protected int calculateFare(int dummyFare) {
        return DistanceFare.calculate(distance);
    }

    private enum DistanceFare {
        BASE(10, (distance) -> BASE_FARE),
        SUBURBAN(50, (distance) -> BASE_FARE + ((int) Math.ceil((double) (distance - 10) / 5) * 100)),
        URBAN(Integer.MAX_VALUE, (distance) -> SUBURBAN_FARE + ((int) Math.ceil((double) (distance - 50) / 8) * 100));

        private final int range;
        private final Function<Integer, Integer> calculator;

        DistanceFare(int range, Function<Integer, Integer> calculator) {
            this.range = range;
            this.calculator = calculator;
        }

        public static int calculate(int distance) {
            DistanceFare distanceFare = Arrays.stream(values())
                .filter(it -> it.range >= distance)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

            return distanceFare.calculator.apply(distance);
        }
    }
}
