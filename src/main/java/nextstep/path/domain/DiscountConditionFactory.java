package nextstep.path.domain;

import nextstep.member.domain.AgeType;

public class DiscountConditionFactory {


    public static DiscountCondition createDiscountCondition(AgeType ageType) {
        if (AgeType.CHILD == ageType) {
            return new ChildDiscountCondition();
        } else if (AgeType.TEENAGER == ageType) {
            return new TeenagerDiscountCondition();
        }
        return new NoneDiscountCondition();
    }
}
