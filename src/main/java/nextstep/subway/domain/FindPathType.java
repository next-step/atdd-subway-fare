package nextstep.subway.domain;

import java.util.Arrays;

public enum FindPathType {
    DURATION, DISTANCE;

    public static FindPathType find(String typeName) {
        return Arrays.stream(FindPathType.values())
                .filter(eachFindPathType -> eachFindPathType.name().equals(typeName))
                .findFirst()
                .orElse(FindPathType.DISTANCE);
    }
}
