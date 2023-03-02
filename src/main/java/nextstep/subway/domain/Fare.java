package nextstep.subway.domain;

import nextstep.subway.domain.exceptions.NegativeNumberException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare implements Comparable<Fare> {
    public static Fare DEFAULT_FARE = Fare.of(1_250);
    public static Fare ZERO_FARE = Fare.of(0);
    public static Fare AGE_DISCOUNT_EXEMPTION_FARE = Fare.of(350);

    @Column(name = "fare")
    private int value;

    public Fare() {
    }

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(int value) {
        if (value < 0) {
            throw new NegativeNumberException("요금은 0보다 적을 수 없습니다");
        }
        return new Fare(value);
    }

    public Fare minus(Fare that) {
        return Fare.of(this.value - that.value);
    }

    public Fare plus(Fare that) {
        return Fare.of(this.value + that.value);
    }

    public Fare ofPercent(int percent) {
        return Fare.of(this.value * percent / 100);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Fare that) {
        return Integer.valueOf(this.value).compareTo(that.value);
    }
}
