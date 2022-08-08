package nextstep.subway.domain;

import javax.persistence.Embeddable;

import java.util.Objects;

import static org.hibernate.type.IntegerType.ZERO;

@Embeddable
public class Duration {
    public static final String DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE = "Duration cannot have a negative or zero value.";

    private int duration;

    protected Duration() {
    }

    public Duration(int duration) {
        if (duration <= ZERO) {
            throw new IllegalArgumentException(DURATION_NOT_NEGATIVE_AND_ZERO_MESSAGE);
        }
        this.duration = duration;
    }

    public void minus(Duration duration) {
        this.duration = Math.abs(this.duration - duration.duration);
    }

    public void plus(Duration duration) {
        this.duration = Math.abs(this.duration + duration.duration);
    }

    public int value() {
        return this.duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration duration1 = (Duration) o;
        return duration == duration1.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration);
    }
}
