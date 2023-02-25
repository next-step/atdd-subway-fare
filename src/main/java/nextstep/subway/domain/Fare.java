package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Fare {

    public static final Fare BASE_FARE =  Fare.from(new BigDecimal("1250"));

    private BigDecimal fare;

    private Fare() {}

    private Fare(final BigDecimal fare) {
        this.fare = fare;
    }

    public static Fare from(final BigDecimal fare) {
        return new Fare(fare);
    }

    public static Fare from(final Long fare) {
        return new Fare(new BigDecimal(fare));
    }

    public static Fare from(final int fare) {
        return new Fare(new BigDecimal(fare));
    }

    public BigDecimal value() {
        return fare;
    }

    public Fare plus(final Fare fare) {
        return new Fare(this.fare.add(fare.fare));
    }

    public BigDecimal getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return Objects.equals(fare, fare1.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
