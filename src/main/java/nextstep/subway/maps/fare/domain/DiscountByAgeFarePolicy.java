package nextstep.subway.maps.fare.domain;

import nextstep.subway.members.member.domain.Member;

public class DiscountByAgeFarePolicy implements FarePolicy {
    @Override
    public void calculate(FareContext fareContext) {
        Member member = fareContext.getMember();
        if (member == null) {
            return;
        }

        Integer age = member.getAge();
        Fare fare = fareContext.getFare();

        if (age >= 19) {
            return;
        }

        if (age >= 13) {
            fare.discountFare(350);
            fare.discountPercent(20);
        }

        if (age >= 6 && age < 13) {
            fare.discountFare(350);
            fare.discountPercent(50);
        }

        fare.discountPercent(100);
    }
}
