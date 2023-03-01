package nextstep.subway.domain;

import nextstep.subway.domain.exceptions.NotPositiveNumberException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Duration {

    @Column(name = "duration")
    private int value;

    public static Duration of(int value) {
        if (value <= 0) {
            throw new NotPositiveNumberException("소요시간은 0보다 커야 합니다");
        }
        return new Duration(value);
    }

    public Duration() {
    }

    private Duration(int value) {
        this.value = value;
    }

    public Duration minus(Duration that) {
        return Duration.of(this.value - that.value);
    }

    public Duration plus(Duration that) {
        return Duration.of(this.value + that.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration distance = (Duration) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
