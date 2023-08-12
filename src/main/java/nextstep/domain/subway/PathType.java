package nextstep.domain.subway;

import java.util.function.Function;

public enum PathType {
    DISTANCE("DISTANCE", Section::getDistance),
    DURATION("DURATION", Section::getDuration);

    private final String type;
    private final Function<Section, Long> weightFunction;

    PathType(String type,Function<Section, Long> weightFunction) {
        this.type = type;
        this.weightFunction = weightFunction;
    }

    public Long getWeight(Section section) {
        return weightFunction.apply(section);
    }

}
