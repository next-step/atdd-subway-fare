package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MemberDiscountPolicy {
    EARLY_CHILD(0, 0, 1),
    CHILD(6, 350, 0.5),
    ADOLESCENCE(13, 350, 0.2),
    ADULT(20, 0, 0);

    private final int age;
    private final int deduction;
    private final double rate;

    MemberDiscountPolicy(int age, int deduction, double rate) {
        this.age = age;
        this.deduction = deduction;
        this.rate = rate;
    }

    public int getAge() {
        return age;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getRate() {
        return rate;
    }

    public static MemberDiscountPolicy decide(int age) {
        List<MemberDiscountPolicy> policies = Arrays.asList(values());
        Collections.reverse(policies);

        return policies.stream()
                .filter(s -> s.isWithinBoundary(age))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    private boolean isWithinBoundary(int age) {
        return age >= this.age;
    }

    public int calculateDiscountFare(int beforeDiscountFare) {
        return (int) Math.ceil((beforeDiscountFare - deduction) * rate);
    }
}
