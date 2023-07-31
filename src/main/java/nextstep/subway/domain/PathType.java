package nextstep.subway.domain;

public enum PathType {
    DISTANCE("거리 기준"),
    DURATION("시간 기준");

    private final String description;

    PathType(String description) {
        this.description = description;
    }
}
