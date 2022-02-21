package nextstep.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FeePolicy {
    public static final int BASIC_FEE = 1250;
    public static final int ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST = 10;
    public static final int ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND = 50;
    public static final int DEDUCTION_FEE = 350;

    public int totalFee(int distance) {
        return totalFee(distance, 0, 19);
    }

    public int totalFee(int distance, int additionalFee) {
        return totalFee(distance, additionalFee, 19);
    }

    public int totalFee(int distance, int additionalFee ,int age) {
        int fee = lineAdditionalFeePolicy(BASIC_FEE, additionalFee);
        fee = distanceFeePolicy(fee, distance);
        fee = ageAdditionalFeePolicy(fee, age);
        return fee;
    }

    private int distanceFeePolicy(int fee, int distance) {
        if (distance > ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND) {
            return fee + 800 + getAdditionalFeePerEight(distance - ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND);
        }
        if (distance > ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST) {
            return fee + getAdditionalFeePerFive(distance - ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST);
        }
        return fee;
    }

    private int lineAdditionalFeePolicy(int fee, int additionalFee) {
        return fee + additionalFee;
    }

    private int ageAdditionalFeePolicy(int fee, int age) {
        if (age >= 6 && age < 13) {
            return (int) ((fee - DEDUCTION_FEE) * 0.5);
        }

        if (age >= 13 && age < 19) {
            return (int) ((fee - DEDUCTION_FEE) * 0.8);
        }
        return fee;
    }

    private int getAdditionalFeePerEight(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    private int getAdditionalFeePerFive(int distance) { return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100); }
}
