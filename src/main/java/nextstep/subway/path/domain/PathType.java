package nextstep.subway.path.domain;

import nextstep.subway.section.domain.Section;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final Function<Section, Integer> sectionMethod;

    PathType(Function<Section, Integer> sectionMethod) {
        this.sectionMethod = sectionMethod;
    }

    private static final Map<String, PathType> NAME_TO_PATH_TYPE = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(Enum::name, pathType -> pathType));

    public static PathType of(String type) {
        if (!NAME_TO_PATH_TYPE.containsKey(type)) {
            throw new IllegalArgumentException("지원되지 않는 경로 조회 타입입니다.");
        }

        return NAME_TO_PATH_TYPE.get(type);
    }

    public int getEdgeValue(Section section) {
        return sectionMethod.apply(section);
    }
}
