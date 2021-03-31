package nextstep.subway.path.domain.specification.discount;

import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Fare;

public interface AgeSpecification {

    Fare discount(Fare fare);

    boolean apply(Age age);
}
