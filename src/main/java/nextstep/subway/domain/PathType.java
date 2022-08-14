package nextstep.subway.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.function.Function;

@AllArgsConstructor
public enum PathType {
    DURATION(Section::getDuration),
    DISTANCE(Section::getDistance);

    private final Function<Section, Integer> function;

    @JsonCreator
    public static PathType from(String value) {
        return Arrays.stream(PathType.values())
                     .filter(type -> type.name().equals(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 경로 조회 타입 입니다."));
    }

    public int getValue(Section section) {
        return function.apply(section);
    }
}
