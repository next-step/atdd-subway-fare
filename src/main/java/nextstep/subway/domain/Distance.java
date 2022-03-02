package nextstep.subway.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
