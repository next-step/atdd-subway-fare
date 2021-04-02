package nextstep.subway.path.domain.specification.discount;

import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Fare;

public class NoDiscount implements AgeSpecification {

    @Override
    public Fare discount(Fare fare) {
        return fare;
    }

    @Override
    public boolean apply(Age age) { return true; }
}
