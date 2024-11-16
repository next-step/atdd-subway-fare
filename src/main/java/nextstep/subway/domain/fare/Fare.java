package nextstep.subway.domain.fare;

public class Fare {
    private static final long DEFAULT_FARE = 1250L;

    private Long fare;

    public Fare() {
        this.fare = DEFAULT_FARE;
    }

    public Fare(Long fare) {
        this.fare = fare;
    }

    public void addFare(Long fare) {
        this.fare += fare;
    }

    public void discountFare(int percentage) {
        if (percentage == 0 || percentage > 100) {
            throw new IllegalArgumentException("할인율은 1~100 사이의 값만 적용할 수 있습니다.");
        }

        this.fare -= 350L;
        this.fare = fare * percentage / 100L;
    }

    public Long getFare() {
        return fare;
    }
}
