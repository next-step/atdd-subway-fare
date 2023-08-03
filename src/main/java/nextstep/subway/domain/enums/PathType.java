package nextstep.subway.domain.enums;

import nextstep.subway.domain.Section;

public enum PathType {

    DISTANCE {
        @Override
        public double getValue(Section section) {
            return section.getDistance();
        }
    }, DURATION {
        @Override
        public double getValue(Section section) {
            return section.getDuration();
        }
    };

    public boolean isDistance() {
        return this == DISTANCE;
    }

    public abstract double getValue(Section section);
}
