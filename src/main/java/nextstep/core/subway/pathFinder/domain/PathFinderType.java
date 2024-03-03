package nextstep.core.subway.pathFinder.domain;

public enum PathFinderType {
    DISTANCE("DISTANCE"),
    DURATION("DURATION");

    private String type;

    PathFinderType(String type) {
        this.type = type;
    }
}
