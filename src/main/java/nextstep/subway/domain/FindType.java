package nextstep.subway.domain;

import java.util.stream.Stream;

public enum FindType {

    DISTANCE("distance"),
    DURATION("duration"),
    ;

    private final String type;

    FindType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static FindType getFindTypeFromTypeName(final String type) {
        return Stream.of(FindType.values())
                .filter(findType -> findType.getType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
