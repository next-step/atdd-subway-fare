package nextstep.subway.path.domain.specification.discount;

import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Fare;

class ChildDiscount implements AgeSpecification {
    private static final Fare BASE_DISCOUNT = Fare.of(350);
    private static final float DISCOUNT_RATIO = 0.2f;
    private static final int MINIMUM_AGE = 13;
    private static final int MAXIMUM_AGE = 19;

    @Override
    public Fare discount(Fare fare) {
        return Fare.multiply(Fare.subtract(fare, BASE_DISCOUNT), (1.0f - DISCOUNT_RATIO));
    }

    @Override
    public boolean apply(Age age) {
        return age.isGreatEqualThan(MINIMUM_AGE) && age.isLessThan(MAXIMUM_AGE);
    }
}