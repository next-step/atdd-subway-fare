package nextstep.subway.domain;

public class Fare {
    public static final Fare ZERO = new Fare(0);

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 음수가 될 수 없습니다.");
        }
        return new Fare(fare);
    }

    public Fare add(Fare other) {
        return new Fare(this.fare + other.fare);
    }

    public int getFare() {
        return fare;
    }
}
