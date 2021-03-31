package nextstep.subway.path.domain.specification.discount;

import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Fare;

class YouthDiscount implements AgeSpecification {
    private static final int BASE_DISCOUNT = 350;
    private static final float DISCOUNT_RATIO = 0.5f;
    private static final int MINIMUM_AGE = 6;
    private static final int MAXIMUM_AGE = 13;

    @Override
    public Fare discount(Fare fare) {
        int discountFare = (int) ((Fare.parseInt(fare) - BASE_DISCOUNT) * (1.0f - DISCOUNT_RATIO));
        return Fare.of(discountFare);
    }

    @Override
    public boolean apply(Age age) {
        final int ageInt = Age.parseInt(age);
        return ageInt >= MINIMUM_AGE && ageInt < MAXIMUM_AGE;
    }
}