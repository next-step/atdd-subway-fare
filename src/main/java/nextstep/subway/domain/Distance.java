package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final int BASE_DISTANCE = 0;
    @Column
    private int distance;

    protected Distance() { }

    private Distance(int distance) {
        validateLessThanZero(distance);
        this.distance = distance;
    }

    private void validateLessThanZero(int distance) {
        if (distance <= BASE_DISTANCE) {
            throw new IllegalArgumentException("거리는 0 이하가 될 수 없습니다. 입력된 거리 : " + distance);
        }
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public Distance decrease(Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public Distance increase(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public int getDistance() {
        return distance;
    }
}
