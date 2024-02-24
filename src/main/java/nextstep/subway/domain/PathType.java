package nextstep.subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum PathType {
    DISTANCE(Section::distance),
    DURATION(Section::duration);

    private Function<Section, Long> type;

    public static PathType of(String pathType) {
        return Arrays.stream(PathType.values())
                .filter(type -> type.name().equals(pathType))
                .findFirst()
                .orElse(DISTANCE);
    }

}
