package nextstep.subway.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AgeDiscountPolicy {

    private int fare;

    public static final int YOUTH_START_AGE = 13;
    public static final int YOUTH_END_AGE = 18;
    public static final int CHILDREN_START_AGE = 6;
    public static final int CHILDREN_END_AGE = 12;

    protected AgeDiscountPolicy(int fare) {
        this.fare = fare;
    }

    public static AgeDiscountPolicy create(int fare, int age) {

        if (age >= CHILDREN_START_AGE && age <= CHILDREN_END_AGE) {
            return new ChildrenDiscountPolicy(fare);
        }

        if (age >= YOUTH_START_AGE && age <= YOUTH_END_AGE) {
            return new YouthDiscountPolicy(fare);
        }
        return null;
    }

    public abstract int discount();
    public int getFare() {
        return fare;
    }
}
