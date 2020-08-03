package nextstep.subway.maps.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Money {

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
}
