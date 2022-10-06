package nextstep.subway.util.pathfinder;

import java.util.Arrays;

public enum PathType {
    DISTANCE("DISTANCE"), DURATION("DURATION"), ARRIVAL_TIME("ARRIVAL_TIME");

    PathType(String name) {
        this.name = name;
    }

    private final String name;

    public static PathType from(String pathType) {
        return Arrays.stream(PathType.values())
                .filter(provider -> provider.equalsName(pathType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean equalsName(String pathType) {
        return this.name.equals(pathType);
    }
}
