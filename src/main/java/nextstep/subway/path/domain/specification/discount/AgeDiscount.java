package nextstep.subway.path.domain.specification.discount;

import com.google.common.collect.Lists;
import nextstep.subway.path.domain.DiscountCondition;
import nextstep.subway.path.domain.Discount;
import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Fare;

import java.util.List;
import java.util.Optional;

public class AgeDiscount implements Discount {

    private final List< AgeSpecification > discountSpecifications = Lists.newArrayList(
            new ChildDiscount(),
            new YouthDiscount()
    );

    public AgeDiscount() {}

    public Fare discount(Fare fare, DiscountCondition condition) {
        return getDiscountSpec((Age) condition.getCondition())
                .map(spec -> spec.discount(fare))
                .orElse(fare);
    }

    private Optional< AgeSpecification > getDiscountSpec(Age age) {
        return discountSpecifications.stream()
                .filter(spec -> spec.apply(age))
                .findFirst();
    }
}
