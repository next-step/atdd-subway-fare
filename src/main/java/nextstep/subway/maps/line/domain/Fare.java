package nextstep.subway.maps.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    @Column(nullable = false)
    private Integer amount = 0;

    public Fare() {
        amount = 0;
    }

    public Fare(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public Fare plus(Fare other) {
        return new Fare(amount + other.amount);
    }

    public Fare minus(Fare other) {
        if (amount < other.amount) {
            return this;
        }
        return new Fare(amount - other.amount);
    }

    public Fare discountByRatio(int ratio) {
        double actualRatio = (100 - ratio) / 100.0;
        return new Fare((int) (actualRatio * amount));
    }
}
