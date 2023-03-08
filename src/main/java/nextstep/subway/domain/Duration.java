package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.domain.exception.DurationCreateException;

@Embeddable
public class Duration {
    private static final int MIN = 1;

    @Column(name = "duration")
    private int value;

    public Duration() {
    }

    public Duration(final int value) {
        if(value < MIN) {
            throw new DurationCreateException(MIN);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
