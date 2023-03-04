package nextstep.subway.domain;

public enum ShortestPathType {
    DISTANCE, // 최단 거리
    TIME;      // 최단 시간

    public int getWeight(Section section) {
        return this == DISTANCE ? section.getDistance() : section.getDuration();
    }
}
