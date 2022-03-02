package nextstep.subway.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Duration {

    private int duration;

    public Duration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
