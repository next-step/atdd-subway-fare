package nextstep.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FeePolicy {
    public static final int BASIC_FEE = 1250;
    public static final int ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST = 10;
    public static final int ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND = 50;

    public int totalFee(int distance) {
        return totalFee(distance, 0, 19);
    }

    public int totalFee(int distance, int additionalFee) {
        return totalFee(distance, additionalFee, 19);
    }

    public int totalFee(int distance, int additionalFee ,int age) {
        if (distance > ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND) {
            return BASIC_FEE + 800 + getAdditionalFeePerEight(distance - ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND);
        }
        if (distance > ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST) {
            return BASIC_FEE + getAdditionalFeePerFive(distance - ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST);
        }
        return BASIC_FEE;
    }

    private int getAdditionalFeePerEight(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    private int getAdditionalFeePerFive(int distance) { return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100); }
}
