package nextstep.subway.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Fare {

    private BigDecimal fare;

    protected Fare() {
    }

    private Fare(BigDecimal fare) {
        this.fare = fare;
    }

    public static Fare of(BigDecimal fare) {
        return new Fare(fare);
    }

    public BigDecimal getFare() {
        return this.fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fare fare1 = (Fare) o;

        return fare.equals(fare1.fare);
    }

    @Override
    public int hashCode() {
        return fare.hashCode();
    }

}
