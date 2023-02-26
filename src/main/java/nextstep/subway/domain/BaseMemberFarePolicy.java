package nextstep.subway.domain;

import nextstep.member.domain.Member;

import java.math.BigDecimal;
import java.util.Objects;

public class BaseMemberFarePolicy implements MemberFarePolicy {
    public static final int EXEMPTION_AMOUNT = 350;
    private static final double DISCOUNT_RATE_50 = 0.5;
    private static final double DISCOUNT_RATE_20 = 0.8;

    private final FarePolicy farePolicy;

    public BaseMemberFarePolicy(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    @Override
    public int calculate(int distance) {
        return farePolicy.calculate(distance);
    }

    @Override
    public int calculate(Member member, int distance) {
        int fare = calculate(distance);
        if (Objects.isNull(member)) {
            return fare;
        }
        return discount(member, fare);
    }

    private int discount(Member member, int fareAmount) {
        BigDecimal fare = BigDecimal.valueOf(fareAmount).subtract(BigDecimal.valueOf(EXEMPTION_AMOUNT));
        if (member.isChildren()) {
            return fare.multiply(BigDecimal.valueOf(DISCOUNT_RATE_50)).intValue();
        }
        if (member.isTeenager()) {
            return fare.multiply(BigDecimal.valueOf(DISCOUNT_RATE_20)).intValue();
        }
        return fare.intValue();
    }
}
