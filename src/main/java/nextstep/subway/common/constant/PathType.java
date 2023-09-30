package nextstep.subway.common.constant;

public enum PathType {

    DURATION("DURATION"),
    DISTANCE("DISTANCE");

    private final String name;

    PathType(String name) { this.name = name; }

    public String getName() { return name; }
}