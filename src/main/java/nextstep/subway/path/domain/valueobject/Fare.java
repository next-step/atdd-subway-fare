package nextstep.subway.path.domain.valueobject;

import java.util.Objects;

public class Fare {
    private int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int fare) {
        return new Fare(fare);
    }

    public static Fare sum(Fare first, Fare second) {
        return Fare.of(first.fare + second.fare);
    }

    public static int parseInt(Fare fare) {
        return fare.getFare();
    }

    private int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
