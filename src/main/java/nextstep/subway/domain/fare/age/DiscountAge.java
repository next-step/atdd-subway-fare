package nextstep.subway.domain.fare.age;

import java.math.BigDecimal;

public interface DiscountAge {

    boolean isTarget(int age);

    int discount(int fare);
}
