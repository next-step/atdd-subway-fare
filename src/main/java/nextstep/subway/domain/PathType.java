package nextstep.subway.domain;

import java.util.Arrays;

public enum PathType {
    DISTANCE,
    DURATION;

    static PathType of(String type) {
        return Arrays.stream(values())
                .filter(v -> type.equalsIgnoreCase(v.name()))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                String.format("%s는 경로 조회 타입에 포함되지 않습니다", type)));
    }

    public int getEdgeType(int distance, int duration) {
        if (this == DISTANCE) {
            return distance;
        }

        return duration;
    }
}
