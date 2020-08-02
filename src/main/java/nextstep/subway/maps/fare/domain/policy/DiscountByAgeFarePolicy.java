package nextstep.subway.maps.fare.domain.policy;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;
import nextstep.subway.members.member.domain.Member;

public class DiscountByAgeFarePolicy implements FarePolicy {
    private static final int DISCOUNT_AGE_MAX = 19;
    private static final int DISCOUNT_YOUTH_AGE_MIN = 13;
    private static final int DISCOUNT_CHILDREN_AGE_MIN = 6;

    @Override
    public void calculate(FareContext fareContext) {
        Member member = fareContext.getMember();
        if (member == null) {
            return;
        }

        Integer age = member.getAge();
        Fare fare = fareContext.getFare();

        if (age >= DISCOUNT_AGE_MAX) {
            return;
        }

        if (age >= DISCOUNT_YOUTH_AGE_MIN) {
            fare.discountFare(350);
            fare.discountPercent(20);
            return;
        }

        if (age >= DISCOUNT_CHILDREN_AGE_MIN) {
            fare.discountFare(350);
            fare.discountPercent(50);
            return;
        }

        fare.discountPercent(100);
    }
}
