package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.domain.exception.DistanceCreateException;

@Embeddable
public class Distance {
    private static final int MIN = 1;

    @Column(name = "distance")
    private int value;

    protected Distance() {
    }

    public Distance(final int value) {
        if (value < MIN) {
            throw new DistanceCreateException(MIN);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
