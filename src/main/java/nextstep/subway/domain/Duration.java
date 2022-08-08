package nextstep.subway.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Duration {

    private int duration;

    protected Duration() { }

    private Duration(int duration) {
        validateLessThanZero(duration);
        this.duration = duration;
    }

    public static Duration from(int duration) {
        return new Duration(duration);
    }

    private void validateLessThanZero(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("소요 시간은 0 이하일 수 없습니다.");
        }
    }

    public int duration() {
        return duration;
    }
}
