package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    private static final int BASE_FARE = 0;

    @Column
    private int fare;

    protected Fare() { }

    private Fare(int fare) {
        validateNegativeFare(fare);
        this.fare = fare;
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    private void validateNegativeFare(int fare) {
        if (fare < BASE_FARE) {
            throw new IllegalArgumentException("노선의 추가 요금은 0 미만의 금액이 들어올 수 없습니다. 입력된 금액 : " + fare);
        }
    }

    public int fare() {
        return fare;
    }
}
