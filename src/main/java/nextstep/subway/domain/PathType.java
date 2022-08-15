package nextstep.subway.domain;

public enum PathType {
    DISTANCE,
    DURATION;

    public int sectionWeight(Section section) {
        if (DISTANCE.equals(this)) {
            return section.getDistance();
        }
        if (DURATION.equals(this)) {
            return section.getDuration();
        }
        throw new IllegalArgumentException("지원하지 않는 경로 타입입니다.");
    }
}
