package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberType;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

@Component
@Priority(3)
public class AgeDiscountFarePolicy implements FarePolicy {
    @Override
    public boolean support(FarePolicyContext context) {
        return context.getMember() != null;
    }

    @Override
    public Fare invoke(FarePolicyContext context, Fare fare) {
        Member member = context.getMember();
        if (member == null || member.getAge() == null) {
            return fare;
        }

        Integer age = member.getAge();
        MemberType memberType = MemberType.getMemberType(age);

        if (memberType.equals(MemberType.TEENAGER)) {
            fare.discountFare(80);
        } else if (memberType.equals(MemberType.CHILDREN)) {
            fare.discountFare(50);
        }

        return fare;
    }
}
