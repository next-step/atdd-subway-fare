package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum PathType {
    DISTANCE(section -> section.getDistance()),
    DURATION(section -> section.getDuration());

    private final Function<Section, Integer> wightFunction;

    public Integer getWight(Section section) {
        return wightFunction.apply(section);
    }
}
