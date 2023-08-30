package nextstep.subway.path.domain.policy.discount;

import org.springframework.stereotype.Component;

@Component
public class DiscountPolicyFactory {
    public DiscountPolicy createBy(Integer age) {
        if (AgeType.isTeenager(age)) {
            return new TeenagerDiscountPolicy();
        }

        if (AgeType.isChildren(age)) {
            return new ChildrenDiscountPolicy();
        }

        return createDefaultDiscountPolicy();
    }

    public DiscountPolicy createDefaultDiscountPolicy() {
        return new DefaultDiscountPolicy();
    }
}
