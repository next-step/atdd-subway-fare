package nextstep.subway.path.domain.valueobject;

import java.util.Objects;

public class Distance {
    private int value;

    private Distance(int distance) {
        this.value = distance;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    public int getDistance() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return value == distance1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
