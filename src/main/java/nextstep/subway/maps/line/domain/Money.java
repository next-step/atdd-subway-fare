package nextstep.subway.maps.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Money implements Comparable<Money> {

    private int value;

    protected Money() {
    }

    public Money(int value) {
        this.value = value;
    }

    public static Money drawNewMoney(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("money cannot be less then zero.");
        }
        return new Money(value);
    }

    public static Money NO_VALUE() {
        return new Money(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Money money = (Money)o;
        return value == money.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Money money) {
        return Integer.compare(this.value, money.value);
    }
}
