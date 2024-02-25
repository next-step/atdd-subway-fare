package nextstep.subway.path.domain;

import nextstep.subway.line.section.domain.Section;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::distance),
    DURATION(Section::duration);

    private final Function<Section, Long> valueExtractor;

    PathType(Function<Section, Long> valueExtractor) {
        this.valueExtractor = valueExtractor;
    }

    public Long findBy(Section section) {
        return valueExtractor.apply(section);
    }
}
