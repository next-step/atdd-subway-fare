package nextstep.subway.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() { }

    private Distance(int distance) {
        validateLessThanZero(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    private void validateLessThanZero(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0 이하가 될 수 없습니다.");
        }
    }

    public int distance() {
        return distance;
    }
}
