package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Money;

@FunctionalInterface
public interface DiscountPolicy {

    Money discount(Money money);
}
