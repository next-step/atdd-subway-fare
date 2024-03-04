package nextstep.core.subway.pathFinder.domain;

import java.util.Arrays;

public enum PathFinderType {
    DISTANCE("DISTANCE"),
    DURATION("DURATION");

    private final String type;

    PathFinderType(String type) {
        this.type = type;
    }

    public static PathFinderType findType(String pathFinderType) {
        return Arrays.stream(values())
                .filter(type -> type.name().equals(pathFinderType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("경로 조회 타입이 아닙니다."));
    }

    public String getType() {
        return type;
    }
}
