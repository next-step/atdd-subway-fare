package nextstep.subway.path.domain;

import nextstep.subway.line.section.domain.Section;

public enum PathType {
    DISTANCE,
    DURATION;

    public Long findBy(Section section) {
        if(this == DISTANCE) {
            return section.distance();
        }
        return section.duration();
    }
}
