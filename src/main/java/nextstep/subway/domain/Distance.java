package nextstep.subway.domain;

import nextstep.subway.domain.exceptions.NotPositiveNumberException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private static int MIN_DISTANCE_VALUE = 1;

    @Column(name = "distance")
    private int value;

    public static Distance of(int value) {
        if (value < MIN_DISTANCE_VALUE) {
            throw new NotPositiveNumberException("거리는 " + MIN_DISTANCE_VALUE + "보다 작을 수 없습니다");
        }
        return new Distance(value);
    }

    public Distance() {
    }

    private Distance(int value) {
        this.value = value;
    }

    public Distance minus(Distance that) {
        return Distance.of(this.value - that.value);
    }

    public Distance plus(Distance that) {
        return Distance.of(this.value + that.value);
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
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
