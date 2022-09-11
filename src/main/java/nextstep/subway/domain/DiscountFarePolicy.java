package nextstep.subway.domain;

import lombok.Getter;

@Getter
public abstract class DiscountPolicy {

    private int fare;

    public static final int YOUTH_START_AGE = 13;
    public static final int YOUTH_END_AGE = 18;
    public static final int CHILDREN_START_AGE = 6;
    public static final int CHILDREN_END_AGE = 12;

    protected DiscountPolicy(int fare) {
        this.fare = fare;
    }

    public static DiscountPolicy create(int fare, int age) {

        if (age >= CHILDREN_START_AGE && age <= CHILDREN_END_AGE) {
            return new ChildrenDiscountPolicy(fare);
        }

        if (age >= YOUTH_START_AGE && age <= YOUTH_END_AGE) {
            return new YouthDiscountPolicy(fare);
        }

        return new DefaultDiscountPolicy(fare);
    }

    public abstract int discount();

}
