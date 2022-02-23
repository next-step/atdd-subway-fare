package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MemberDiscountPolicy {
    CHILD(6, 0.5),
    ADOLESCENCE(13, 0.2),
    ADULT(20, 0);

    private final int age;
    private final double rate;

    MemberDiscountPolicy(int age, double rate) {
        this.age = age;
        this.rate = rate;
    }

    public int getAge() {
        return age;
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

    public int calculateDiscountFare(int beforeDiscountFare, int basicDeductionFare) {
        return (int) Math.ceil((beforeDiscountFare - basicDeductionFare) * rate);
    }
}
