package nextstep.subway.domain.service.path;

import lombok.AllArgsConstructor;
import nextstep.subway.domain.StationLineSection;

import java.util.Objects;
import java.util.function.Function;

@AllArgsConstructor
public enum StationPathSearchRequestType {
    DISTANCE(StationLineSection::getDistance),
    DURATION(StationLineSection::getDuration);

    private final Function<StationLineSection, Number> weightFunction;

    public Number calculateWeightOf(StationLineSection section) {
        if (Objects.isNull(section)) {
            throw new IllegalArgumentException("section should not be null");
        }

        return weightFunction.apply(section);
    }
}
