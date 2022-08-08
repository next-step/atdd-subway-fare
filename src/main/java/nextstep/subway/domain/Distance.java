package nextstep.subway.domain;

import javax.persistence.Embeddable;

import java.util.Objects;

import static org.hibernate.type.IntegerType.ZERO;

@Embeddable
public class Distance {
    public static final String DISTANCE_NOT_NEGATIVE_AND_ZERO_MESSAGE = "Distance cannot have a negative or zero value.";

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        if (distance <= ZERO) {
            throw new IllegalArgumentException(DISTANCE_NOT_NEGATIVE_AND_ZERO_MESSAGE);
        }

        this.distance = distance;
    }

    public void minus(Distance distance) {
        this.distance = Math.abs(this.distance - distance.distance);
    }

    public void plus(Distance distance) {
        this.distance = Math.abs(this.distance + distance.distance);
    }

    public int value() {
        return this.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
