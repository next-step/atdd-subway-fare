package nextstep.core.subway.path.domain;

import java.util.Arrays;

public enum PathType {
    DISTANCE("DISTANCE"),
    DURATION("DURATION");

    private final String type;

    PathType(String type) {
        this.type = type;
    }

    public static PathType findType(String pathFinderType) {
        return Arrays.stream(values())
                .filter(type -> type.name().equals(pathFinderType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("경로 조회 타입이 아닙니다."));
    }

    public String getType() {
        return type;
    }
}
