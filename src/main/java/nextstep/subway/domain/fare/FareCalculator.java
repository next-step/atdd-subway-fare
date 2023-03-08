package nextstep.subway.domain.fare;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FareCalculator {

    private final AgeFareStrategy strategy;

    private static final int FIXED_DISCOUNT = 350;

    public int calculateFare(int fare) {
        double weight = strategy.getWeight();

        if (weight == 1) {
            return fare;
        }

        return (int) ((fare - FIXED_DISCOUNT) * weight);
    }
}
