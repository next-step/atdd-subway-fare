package nextstep.subway.ui;

import nextstep.subway.domain.Section;

public enum PathType {
    DISTANCE,
    DURATION,
    ;

    public double weight(final Section section) {
        if (this.equals(DISTANCE)) {
            return section.getDistance();
        } else if (this.equals(DURATION)) {
            return section.getDuration();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
