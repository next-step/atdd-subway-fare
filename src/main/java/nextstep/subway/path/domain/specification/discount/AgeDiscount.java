package nextstep.subway.path.domain.specification.discount;

import com.google.common.collect.Lists;
import nextstep.subway.path.domain.FareSpecification;
import nextstep.subway.path.domain.Discount;
import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Fare;

import java.util.List;
import java.util.Optional;

public class AgeDiscount implements Discount {

    private Optional<AgeSpecification> spec;

    private final List< AgeSpecification > discountSpecifications = Lists.newArrayList(
            new ChildDiscount(),
            new YouthDiscount()
    );

    private AgeDiscount() { }

    public static AgeDiscount of(Age age){
        AgeDiscount discount = new AgeDiscount();
        discount.searchDiscountSpec(age);
        return discount;
    }

    public Fare apply(Fare fare) {
        return spec.map(ageSpec -> ageSpec.discount(fare))
                .orElse(fare);
    }

    private void searchDiscountSpec(Age age) {
        spec = discountSpecifications.stream()
                .filter(spec -> spec.apply(age))
                .findFirst();
    }
}
