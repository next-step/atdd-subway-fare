package nextstep.subway.domain;

import nextstep.subway.ui.exception.SectionException;

import javax.persistence.Embeddable;

import static nextstep.subway.ui.exception.ExceptionMessage.ADD_SECTION_DISTANCE;

@Embeddable
public class Distance {
    private int distance;

    protected Distance() { }

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance subtract(int newDistance) {
        validateBetWeenAddDistance(newDistance);
        return new Distance(distance - newDistance);
    }

    public Distance sum(int newDistance) {
        return new Distance(distance + newDistance);
    }

    private void validateBetWeenAddDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new SectionException(String.format(ADD_SECTION_DISTANCE.getMsg(), this.distance, newDistance));
        }
    }

    public int getDistance() {
        return distance;
    }
}
