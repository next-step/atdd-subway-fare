package nextstep.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FeePolicy {
    public static final int BASIC_FEE = 1250;
    public static final int ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST = 10;
    public static final int ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND = 50;
    public static final int DEDUCTION_FEE = 350;
    public static final int DEFAULT_AGE = 20;
    public static final double CHILD_DISCOUNT_RATE = 0.5;
    public static final double TEENAGER_DISCOUNT_RATE = 0.8;
    public static final int TEENAGER_MIN_AGE = 13;
    public static final int TEENAGER_LIMIT_AGE = 19;
    public static final int CHILD_LIMIT_AGE = 13;
    public static final int CHILD_MIN_AGE = 6;
    public static final int EIGHT = 8;
    public static final int FIVE = 5;

    public int totalFee(int distance) {
        return totalFee(distance, 0, 19);
    }

    public int totalFee(int distance, int additionalFee) {
        return totalFee(distance, additionalFee, DEFAULT_AGE);
    }

    public int totalFee(int distance, int additionalFee ,int age) {
        int fee = lineAdditionalFeePolicy(BASIC_FEE, additionalFee);
        fee = distanceFeePolicy(fee, distance);
        fee = ageAdditionalFeePolicy(fee, age);
        return fee;
    }

    private int distanceFeePolicy(int fee, int distance) {
        if (distance > ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND) {
            return fee + 800 + getAdditionalFeePer(EIGHT, distance - ADDITIONAL_FEE_STANDARD_DISTANCE_SECOND);
        }
        if (distance > ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST) {
            return fee + getAdditionalFeePer(FIVE, distance - ADDITIONAL_FEE_STANDARD_DISTANCE_FIRST);
        }
        return fee;
    }

    private int lineAdditionalFeePolicy(int fee, int additionalFee) {
        return fee + additionalFee;
    }

    private int ageAdditionalFeePolicy(int fee, int age) {
        if (isChild(age)) {
            return (int) ((fee - DEDUCTION_FEE) * CHILD_DISCOUNT_RATE);
        }

        if (isTeenager(age)) {
            return (int) ((fee - DEDUCTION_FEE) * TEENAGER_DISCOUNT_RATE);
        }
        return fee;
    }
    
    private boolean isTeenager(int age) {
        return age >= TEENAGER_MIN_AGE && age < TEENAGER_LIMIT_AGE;
    }

    private boolean isChild(int age) {
        return age >= CHILD_MIN_AGE && age < CHILD_LIMIT_AGE;
    }

    private int getAdditionalFeePer(int number ,int distance) {return (int) ((Math.ceil((distance - 1) / number) + 1) * 100);}
    
}
