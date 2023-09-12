package nextstep.subway.domain;

public enum PathType {

    DISTANCE("거리 기반 경로"),
    DURATION("소요시간 기반 경로")
    ;

    private String description;

    private PathType(String description) {
        this.description = description;
    }
}
