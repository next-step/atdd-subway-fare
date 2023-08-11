package nextstep.subway.domain;

public enum AgeBasedDiscountPolicy {
    FREE(0, 5, 1.0f),
    CHILD_DISCOUNT(6, 12, 0.5f),
    TEENAGER_DISCOUNT(13, 18, 0.2f),
    NO_DISCOUNT(19, Integer.MAX_VALUE, 0.0f);
    private final int minAge;
    private final int maxAge;
    private final float discountRate;

    AgeBasedDiscountPolicy(int minAge, int maxAge, float discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static AgeBasedDiscountPolicy getPolicyByAge(int age) {
        for (AgeBasedDiscountPolicy policy : values()) {
            if (age >= policy.minAge && age <= policy.maxAge) {
                return policy;
            }
        }
        throw new IllegalArgumentException("Invalid age: " + age);
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public int getMinAge() {
        return minAge;
    }
}
