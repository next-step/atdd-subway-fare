package nextstep.subway.domain;

public enum FindType {

    DISTANCE("distance"),
    DURATION("duration"),
    ;

    private final String type;

    FindType(final String type) {
        this.type = type;
    }
}
