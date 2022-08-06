package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration),
    ;

    private final Function<Section, Integer> parser;

    public int parseWeight(Section section) {
        return parser.apply(section);
    }
}
