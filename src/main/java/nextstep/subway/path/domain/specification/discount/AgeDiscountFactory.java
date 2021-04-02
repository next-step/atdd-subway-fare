package nextstep.subway.path.domain.specification.discount;

import com.google.common.collect.Lists;
import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Fare;

import java.util.List;
import java.util.Optional;

public class AgeDiscountFactory implements FarePolicy {

    private Optional<AgeSpecification> spec;

    private final List< AgeSpecification > discountSpecifications = Lists.newArrayList(
            new ChildDiscount(),
            new YouthDiscount()
    );

    private AgeDiscountFactory() { }

    public static AgeDiscountFactory getDiscount(Age age){
        AgeDiscountFactory discount = new AgeDiscountFactory();
        discount.searchDiscountSpec(age);
        return discount;
    }

    @Override
    public Fare calculate(Fare fare) {
        return spec.map(ageSpec -> ageSpec.discount(fare))
                .orElse(fare);
    }

    private void searchDiscountSpec(Age age) {
        spec = Optional.of(discountSpecifications.stream()
                .filter(spec -> spec.apply(age))
                .findFirst()
                .orElse(new NoDiscount()));
    }
}
