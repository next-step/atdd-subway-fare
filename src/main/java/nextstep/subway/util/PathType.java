package nextstep.subway.util;

import java.util.Arrays;

public enum PathType {
    DISTANCE("DISTANCE"), DURATION("DURATION");

    PathType(String name) {
        this.name = name;
    }

    private final String name;

    public static PathType find(String pathType) {
        return Arrays.stream(PathType.values())
                .filter(provider -> provider.equalsName(pathType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean equalsName(String pathType) {
        return this.name.equals(pathType);
    }
}
