package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Fare {

    private static final int BASE_FARE = 1250;
    @Column
    private BigDecimal fare;

    protected Fare() {}

    public Fare(final BigDecimal fare) {
        this.fare = fare;
    }

    public static Fare from(final int fare) {
        return new Fare(BigDecimal.valueOf(fare));
    }

    public static Fare base() {
        return new Fare(BigDecimal.valueOf(BASE_FARE));
    }

    public Fare plus(final Fare fare) {
        return new Fare(this.fare.add(fare.fare));
    }

    public Fare minus(final Fare deduction) {
        return new Fare(this.fare.subtract(deduction.fare));
    }

    public Fare multiply(final Fare discountFare) {
        final BigDecimal amount = this.fare.multiply(discountFare.fare);
        return new Fare(amount.stripTrailingZeros());
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
