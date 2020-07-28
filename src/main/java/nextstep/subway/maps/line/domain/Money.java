package nextstep.subway.maps.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Money {
    public static final Money ZERO = Money.wons(0);

    private int amount = 0;

    public Money() {
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
}
