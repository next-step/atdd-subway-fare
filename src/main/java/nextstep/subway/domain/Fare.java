package nextstep.subway.domain;

import nextstep.subway.domain.exceptions.NegativeNumberException;

import java.util.Objects;

public class Fare {
    private int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(int value) {
        if (value < 0) {
            throw new NegativeNumberException("요금은 0보다 적을 수 없습니다");
        }
        return new Fare(value);
    }

    public Fare plus(Fare that) {
        return Fare.of(this.value + that.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
