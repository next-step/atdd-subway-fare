package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration),
    ;

    public double weight(final Section section) {
        return sectionToWeight.apply(section);
    }

    private final Function<Section, Integer> sectionToWeight;

    PathType(final Function<Section, Integer> sectionToWeight) {
        this.sectionToWeight = sectionToWeight;
    }
}
