package nextstep.subway.maps.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Money implements Comparable<Money> {
    public static final Money ZERO = Money.wons(0);

    private int amount = 0;

    protected Money() {
    }

    private Money(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("money must be positive or zero");
        }
        this.amount = amount;
    }

    public static Money wons(int amount) {
        return new Money(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public int compareTo(Money o) {
        return Integer.compare(this.amount, o.amount);
    }
}
