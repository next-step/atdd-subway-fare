package nextstep.subway.path.domain.valueobject;

import nextstep.subway.path.exception.InvalidFareAmountException;

import java.util.Objects;

public class Fare {
    private int fare;
    private Fare(int fare){
        this.fare = fare;
    }

    public static Fare of(int fare) {
        if ( fare <= 0 ){
            throw new InvalidFareAmountException("Fare should be greater than 0");
        }
        return new Fare(fare);
    }

    private int getFare(){
        return fare;
    }

    public static Fare sum(Fare first, Fare second) {
        return Fare.of(first.fare+second.fare);
    }

    public static Fare subtract(Fare first, Fare second) {
        return Fare.of(first.fare - second.fare);
    }

    public static Fare multiply(Fare first, float ratio) {
        return Fare.of((int) (first.fare*ratio));
    }

    public static int parseInt(Fare fare){
        return fare.getFare();
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

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + fare +
                '}';
    }
}
