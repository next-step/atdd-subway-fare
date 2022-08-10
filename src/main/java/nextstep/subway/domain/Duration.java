package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Duration {

    private static final int BASE_DURATION = 0;
    @Column
    private int duration;

    protected Duration() { }

    private Duration(int duration) {
        validateLessThanZero(duration);
        this.duration = duration;
    }

    private void validateLessThanZero(int duration) {
        if (duration <= BASE_DURATION) {
            throw new IllegalArgumentException("소요 시간은 0 이하일 수 없습니다. 입력된 시간 : " + duration);
        }
    }

    public static Duration from(int duration) {
        return new Duration(duration);
    }

    public Duration decrease(Duration duration) {
        return new Duration(this.duration - duration.duration);
    }

    public Duration increase(Duration duration) {
        return new Duration(this.duration + duration.duration);
    }

    public int getDuration() {
        return duration;
    }
}
